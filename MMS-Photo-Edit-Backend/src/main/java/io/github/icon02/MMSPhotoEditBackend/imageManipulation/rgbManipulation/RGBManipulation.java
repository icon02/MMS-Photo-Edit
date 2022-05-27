package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rgbManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageManipulationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RGBManipulation implements ImageFilter {
    // values in percentage
    private Integer rDif = null; // -100 <= rDif <= 100
    private Integer gDif = null; // -100 <= gDif <= 100
    private Integer bDif = null; // -100 <= bDif <= 100

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (selectionRaster == null || selectionRaster[x][y] != null && selectionRaster[x][y]){
                    Color c = new Color(image.getRGB(x, y));
                    bi.setRGB(x, y, ImageManipulationUtil.applyPercentageColor(c, rDif, gDif, bDif));
                } else{
                    bi.setRGB(x, y,image.getRGB(x, y));
                }
            }
        }
        return bi;
    }

    private void verifyState() {
        if(rDif == null) throw new IllegalStateException("'rDif' must not be null");
        if(rDif < -100 || rDif > 100) throw new IllegalStateException("'rDif' must be between -100 and 100");

        if(gDif == null) throw new IllegalStateException("'gDif' must not be null");
        if(gDif < -100 || gDif > 100) throw new IllegalStateException("'gDif' must be between -100 and 100");

        if(bDif == null) throw new IllegalStateException("'bDif' must not be null");
        if(bDif < -100 || bDif > 100) throw new IllegalStateException("'bDif' must be between -100 and 100");
    }
}
