package io.github.icon02.MMSPhotoEditBackend.mapper;

import io.github.icon02.MMSPhotoEditBackend.dto.CircleSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.FreehandSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.RectSelection;
import io.github.icon02.MMSPhotoEditBackend.utils.Point2D;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HashMapToSelectionMapper {
    public Object toSelectionObject(Object input) {
        if (input instanceof Map) {
            RectSelection rectSelection = getRectSelection((Map) input);
            if (rectSelection != null) return rectSelection;

            CircleSelection circleSelection = getCircleSelection((Map) input);
            if (circleSelection != null) return circleSelection;

            FreehandSelection freeHandSelection = getFreeHandSelection((Map) input);
            if (freeHandSelection != null) return freeHandSelection;
        }

        return null;
    }

    private RectSelection getRectSelection(Map map) {
        Object topLeft = map.get("topLeft");
        if (!(topLeft instanceof Map)) return null;

        Object topLeftX = ((Map) topLeft).get("x");
        if (!(topLeftX instanceof Number)) return null;
        Object topLeftY = ((Map) topLeft).get("y");
        if (!(topLeftY instanceof Number)) return null;

        Object width = map.get("width");
        if (!(width instanceof Number)) return null;
        Object height = map.get("height");
        if (!(height instanceof Number)) return null;

        Integer canvasWidth = getCanvasWidth(map);
        if(canvasWidth == null) return null;

        Integer canvasHeight = getCanvasHeight(map);
        if(canvasHeight == null) return null;

        RectSelection selection = new RectSelection(
                new Point2D(
                        ((Number) topLeftX).intValue(),
                        ((Number) topLeftY).intValue()
                ),
                ((Number) width).intValue(),
                ((Number) height).intValue()
        );
        selection.setCanvasWidth(canvasWidth);
        selection.setCanvasHeight(canvasHeight);

        return selection;
    }

    private CircleSelection getCircleSelection(Map map) {
        Object center = map.get("center");
        if (!(center instanceof Map)) return null;

        Object centerX = ((Map) center).get("x");
        if (!(centerX instanceof Number)) return null;
        Object centerY = ((Map) center).get("y");
        if (!(centerY instanceof Number)) return null;

        Object radius = map.get("radius");
        if (!(radius instanceof Number)) return null;

        Integer canvasWidth = getCanvasWidth(map);
        if(canvasWidth == null) return null;

        Integer canvasHeight = getCanvasHeight(map);
        if(canvasHeight == null) return null;

        CircleSelection selection = new CircleSelection(
                new Point2D(
                        ((Number) centerX).intValue(),
                        ((Number) centerY).intValue()
                ),
                ((Number) radius).intValue()
        );
        selection.setCanvasWidth(canvasWidth);
        selection.setCanvasHeight(canvasHeight);

        return selection;
    }

    private FreehandSelection getFreeHandSelection(Map map) {
        // TODO
        return null;
    }

    private Integer getCanvasWidth(Map map) {
        Object canvasWidth = map.get("canvasWidth");
        if(!(canvasWidth instanceof Number)) return null;
        return ((Number) canvasWidth).intValue();
    }

    private Integer getCanvasHeight(Map map) {
        Object canvasHeight = map.get("canvasHeight");
        if(!(canvasHeight instanceof Number)) return null;
        return ((Number) canvasHeight).intValue();
    }
}
