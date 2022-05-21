package io.github.icon02.MMSPhotoEditBackend.imageManipulation.greyScaleManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GreyScaleManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        //TODO: implement for Raster

        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));
                int avg = (c.getBlue()+c.getGreen()+c.getRed()) / 3;

                bi.setRGB(x, y,(c.getAlpha() << 24| avg << 16 |  avg << 8 | avg));
            }
        }

        return bi;
    }
}
