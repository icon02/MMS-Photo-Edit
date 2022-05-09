package io.github.icon02.MMSPhotoEditBackend.mapper;

import io.github.icon02.MMSPhotoEditBackend.dto.CircleSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.FreehandSelection;
import io.github.icon02.MMSPhotoEditBackend.dto.RectSelection;
import io.github.icon02.MMSPhotoEditBackend.utils.SubSampler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelectionToRasterMapper {

    public Boolean[][] getRaster(Object selection, int reqWidth, int reqHeight) {

        Boolean[][] raster = null;

        if(selection instanceof CircleSelection)
            raster = getRasterFromCircle((CircleSelection) selection);
        else if(selection instanceof RectSelection)
            raster = getRasterFromRect((RectSelection) selection);
        else if(selection instanceof FreehandSelection)
            raster = getRasterFromFreehand((FreehandSelection) selection);

        return new SubSampler<Boolean>().resize(raster, reqWidth, reqHeight, Boolean.class);
    }

    private Boolean[][] getRasterFromCircle(CircleSelection selection) {
        // TODO
        return null;
    }

    private Boolean[][] getRasterFromRect(RectSelection selection) {
        // TODO
        return null;
    }

    private Boolean[][] getRasterFromFreehand(FreehandSelection selection) {
        // TODO
        return null;
    }
}
