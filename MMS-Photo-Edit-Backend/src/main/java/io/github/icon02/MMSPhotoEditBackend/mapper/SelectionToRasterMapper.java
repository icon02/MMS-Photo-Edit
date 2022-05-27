package io.github.icon02.MMSPhotoEditBackend.mapper;

import io.github.icon02.MMSPhotoEditBackend.dto.CanvasSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.CircleSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.FreehandSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.RectSelection;
import io.github.icon02.MMSPhotoEditBackend.utils.Point2D;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SelectionToRasterMapper {

    public Boolean[][] getRaster(Object selection, Integer reqWidth, Integer reqHeight) {
        if (reqWidth == null || reqHeight == null) return null;
        Boolean[][] raster = null;

        if (selection instanceof CircleSelection)
            raster = getRasterFromCircle((CircleSelection) selection, reqWidth, reqHeight);
        else if (selection instanceof RectSelection)
            raster = getRasterFromRect((RectSelection) selection, reqWidth, reqHeight);
        else if (selection instanceof FreehandSelection)
            raster = getRasterFromFreehand((FreehandSelection) selection, reqWidth, reqHeight);

        return raster;
    }

    /**
     * get the distance between two points
     * @param a point A
     * @param b point B
     * @return the distance
     */
    private int getDistance(Point2D a, Point2D b) {
        Point2D vector = new Point2D(b.getX() - a.getX(), b.getY() - a.getY());
        return ((int) Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2)));
    }

    /**
     * calculates the raster within a circle
     * @param selection the selection
     * @return the raster
     */
    private Boolean[][] getRasterFromCircle(CircleSelection selection, int reqWidth, int reqHeight) {
        Boolean[][] result = getEmptyArray(reqWidth, reqHeight);
        selection = ((CircleSelection) getScaledSelection(reqWidth, reqHeight, selection));
        // calculate if the distance from the point to the center of the circle is smaller than the radius and set to true
        for (int x = selection.getCenter().getX() - selection.getRadius(); x <= selection.getCenter().getX() + selection.getRadius(); x++) {
            for (int y = selection.getCenter().getY() - selection.getRadius(); y <= selection.getCenter().getY() + selection.getRadius(); y++) {
                result[x][y] = getDistance(new Point2D(x, y), selection.getCenter()) <= selection.getRadius();
            }
        }
        return result;
    }

    /**
     * calculates the raster within a rectangle
     * @param selection the selection
     * @return the raster
     */
    private Boolean[][] getRasterFromRect(RectSelection selection, int reqWidth, int reqHeight) {
        Boolean[][] result = getEmptyArray(reqWidth, reqHeight);

        selection = ((RectSelection) getScaledSelection(reqWidth, reqHeight, selection));
        for (int x = selection.getTopLeft().getX(); x <= selection.getTopLeft().getX() + selection.getWidth(); x++) {
            for (int y = selection.getTopLeft().getY(); y <= selection.getTopLeft().getY() + selection.getHeight(); y++) {
                result[x][y] = true;
            }
        }
        return result;
    }

    /**
     * calculates the raster within a range of points
     * @param selection the selection
     * @return the raster
     */
    private Boolean[][] getRasterFromFreehand(FreehandSelection selection, int reqWidth, int reqHeight) {
        Boolean[][] result = getEmptyArray(reqWidth, reqHeight);
        int[][] preResult = new int[reqWidth][reqHeight];
        selection = ((FreehandSelection) getScaledSelection(reqWidth, reqHeight, selection));
        Point2D[] vectors = new Point2D[selection.getPoints().size()];
        Point2D[] points = selection.getPoints().toArray(new Point2D[0]);

        //remember the window in which the calculations have to be within
        int minX = reqWidth, minY = reqHeight, maxX = 0, maxY = 0;

        //calculate vectors between points
        for (int i = 0; i < points.length; i++) {
            Point2D a = points[i];
            Point2D b = points[(i + 1) % points.length];

            vectors[i] = new Point2D(b.getX() - a.getX(), b.getY() - a.getY());
            minX = Math.min(minX, a.getX());
            minY = Math.min(minY, a.getY());
            maxX = Math.max(maxX, a.getX());
            maxY = Math.max(maxY, a.getY());
        }

        //draw vectors
        for (int v = 0; v < vectors.length; v++) {
            Point2D vector = vectors[v];
            Point2D point = points[v];

            int longestSide = Math.max(Math.abs(vector.getX()), Math.abs(vector.getY()));
            for (int i = 0; i <= longestSide; i++) {
                double factor = i / ((double) longestSide);
                result[((int) (point.getX() + factor * vector.getX()))][((int) (point.getY() + factor * vector.getY()))] = true;
                preResult[((int) (point.getX() + factor * vector.getX()))][((int) (point.getY() + factor * vector.getY()))] = v + 1;
            }
        }

        //fill out shape
        for (int x = minX; x <= maxX; x++) {
            boolean draw = false;
            int currentVector = 0;

            //fill out each line on its own
            for (int y = minY; y <= maxY; y++) {
                if (preResult[x][y] != 0 && preResult[x][y] != currentVector) {
                    draw = !draw;
                    currentVector = preResult[x][y];
                } else {
                    result[x][y] = draw;
                }
            }

            //clean up som stuff that might have gone wrong
            if (draw){
                boolean revert = false;
                for (int y = minY; y <= maxY; y++) {
                    if (preResult[x][y] != 0) revert = true;
                    if (revert) result[x][y] = !result[x][y];
                }
            }
        }
        return result;
    }

    /**
     * gives an array with all values set to false
     * @return the array
     */
    private Boolean[][] getEmptyArray(int width, int height) {
        Boolean[][] result = new Boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = false;
            }
        }
        return result;
    }

    /**
     * scale the selection from the display size to the actual image size
     * @return a scaled selection
     */
    private CanvasSelection getScaledSelection(int reqWidth, int reqHeight, CanvasSelection selection) {
        //factor is the same for width as for height because image is not stretched
        double factor = ((double) reqWidth) / ((double) selection.getCanvasWidth());

        CanvasSelection result = selection;


        if (selection instanceof CircleSelection) {
            CircleSelection sel = ((CircleSelection) selection);
            result = new CircleSelection(new Point2D(((int) (sel.getCenter().getX() * factor)), ((int) (sel.getCenter().getY() * factor))), ((int) (sel.getRadius() * factor)));

        } else if (selection instanceof RectSelection) {
            RectSelection sel = ((RectSelection) selection);
            result = new RectSelection(new Point2D(((int) (sel.getTopLeft().getX() * factor)), ((int) (sel.getTopLeft().getY() * factor))), ((int) (sel.getWidth() * factor)), ((int) (sel.getHeight() * factor)));

        } else if (selection instanceof FreehandSelection) {
            FreehandSelection sel = ((FreehandSelection) selection);
            result = new FreehandSelection(sel.getPoints().stream().map(p -> new Point2D(((int) (p.getX() * factor)), ((int) (p.getY() * factor)))).collect(Collectors.toList()));
        }

        return result;
    }

}
