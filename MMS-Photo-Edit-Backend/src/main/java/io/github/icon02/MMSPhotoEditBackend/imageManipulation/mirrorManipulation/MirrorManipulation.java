package io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MirrorManipulation implements ImageFilter {

    private Direction direction;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        return mirrorImage(image, selectionRaster);
    }


    /**
     * Mirror (part of) the image.
     *
     * @param image             Original image.
     * @param selectionRaster   Defines the part, that will be mirrored, if not null;
     * @return                  Mirrored image.
     */
    private BufferedImage mirrorImage(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        if (selectionRaster == null) { // whole image
            for (int i = 0; i < newImg.getWidth(); i++) {
                for (int j = 0; j < newImg.getHeight(); j++) {

                    Color c = new Color(image.getRGB(i, j));
                    if (direction == Direction.VERTICAL)
                        newImg.setRGB((newImg.getWidth() - 1) - i, j, c.getRGB());
                    else if (direction == Direction.HORIZONTAL)
                        newImg.setRGB(i, (newImg.getHeight() - 1) - j, c.getRGB());

                }
            }
        } else { // selection of the image
            // fill new image
            for (int i = 0; i < image.getWidth(); i++)
                for (int j = 0; j < image.getHeight(); j++)
                    newImg.setRGB(i, j, image.getRGB(i, j));

            if (direction == Direction.HORIZONTAL) {
                ArrayList<ArrayList<Integer>> selected = rasterRows(selectionRaster);

                for (int i = 0; i < image.getHeight(); i++) {
                    ArrayList<Integer> inner = selected.get(i);

                    if (inner.size() != 0) {
                        int start = inner.get(0);
                        int end = inner.get(inner.size()-1);

                        for (int j = start; j <= end; j++) {
                            Color left = new Color(image.getRGB(i, j));
                            Color right = new Color(image.getRGB(i, end - (j - start)));
                            newImg.setRGB(i, j, right.getRGB());
                            newImg.setRGB(i, end - (j - start), left.getRGB());
                        }
                    }
                }
            } else if (direction == Direction.VERTICAL) {
                ArrayList<ArrayList<Integer>> selected = rasterCols(selectionRaster);

                for (int i = 0; i < image.getHeight(); i++) {
                    ArrayList<Integer> inner = selected.get(i);
                    if (inner.size() != 0) {
                        int start = inner.get(0);
                        int end = inner.get(inner.size()-1);

                        for (int j = start; j < end; j++) {
                            Color left = new Color(image.getRGB(j, i));
                            Color right = new Color(image.getRGB(end - (j - start), i));
                            newImg.setRGB(j, i, right.getRGB());
                            newImg.setRGB(end - (j - start) ,i , left.getRGB());
                        }
                    }
                } // end for1
            } // end else if
        } // end else
        return newImg;
    }

    /**
     * Fetches and stores the coordinates that are selected in the selection raster for horizontal mirroring.
     *
     * @param raster    The selection raster.
     * @return          List of lists of coordinates.
     */
    private ArrayList<ArrayList<Integer>> rasterRows(Boolean[][] raster) {
        ArrayList<ArrayList<Integer>> r = new ArrayList<>();
        for (int i = 0; i < raster.length; i++) {
            r.add(new ArrayList<>());
            for (int j = 0; j < raster[i].length; j++) {
                if (raster[i][j])
                    r.get(i).add(j);
            }
        }
        return r;
    }

    /**
     * Fetches and stores the coordinates that are selected in the selection raster for vertical mirroring.
     *
     * @param raster    The selection raster.
     * @return          List of lists of coordinated.
     */
    private ArrayList<ArrayList<Integer>> rasterCols(Boolean[][] raster) {
        return rasterRows(transpose(raster));
    }

    /**
     * Calculate the transpose of a matrix.
     *
     * @param matrix    The matrix to be transposed.
     * @return          Transposed matrix.
     */
    private Boolean[][] transpose(Boolean[][] matrix) {
        Boolean[][] transposed = new Boolean[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                transposed[j][i] = matrix[i][j];

        return transposed;
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}