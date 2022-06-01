package io.github.icon02.MMSPhotoEditBackend.imageManipulation.brightnessManipulation;

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
public class BrightnessManipulation implements ImageFilter {

    private Areas area;
    private Integer value;


    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));

                if ((area == Areas.ALL
                        || area == Areas.DARK && c.getRed() < 30 && c.getBlue() < 30 && c.getGreen() < 30
                        || area == Areas.BRIGHT && c.getRed() > 225 && c.getBlue() > 225 && c.getGreen() > 225)
                        && (selectionRaster == null || selectionRaster[x][y] != null && selectionRaster[x][y])) {
                    bi.setRGB(x, y, ImageManipulationUtil.applyPercentageColor(c, value, value, value));
                } else {
                    bi.setRGB(x, y, c.getRGB());
                }
            }
        }
        return bi;
    }

    private void verifyState() {
        if (area == null) throw new IllegalStateException("'area' must not be null");
    }

    public enum Areas {
        ALL, BRIGHT, DARK
    }
}
