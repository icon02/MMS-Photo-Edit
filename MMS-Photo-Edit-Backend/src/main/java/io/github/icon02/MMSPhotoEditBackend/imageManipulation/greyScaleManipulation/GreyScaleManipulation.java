package io.github.icon02.MMSPhotoEditBackend.imageManipulation.greyScaleManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GreyScaleManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (selectionRaster == null ||
                        selectionRaster[x][y] != null && selectionRaster[x][y]){
                    Color c = new Color(image.getRGB(x, y));
                    int avg = (c.getBlue()+c.getGreen()+c.getRed()) / 3;

                    bi.setRGB(x, y,(c.getAlpha() << 24| avg << 16 |  avg << 8 | avg));
                } else{
                    bi.setRGB(x, y,image.getRGB(x, y));
                }

            }
        }

        return bi;
    }
}
