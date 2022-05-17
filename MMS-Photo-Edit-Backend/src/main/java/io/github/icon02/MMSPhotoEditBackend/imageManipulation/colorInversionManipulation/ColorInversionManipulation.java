package io.github.icon02.MMSPhotoEditBackend.imageManipulation.colorInversionManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorInversionManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color testColor = new Color(
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255),
                        (int) (Math.random() * 255)
                );

                output.setRGB(x, y, testColor.getRGB());
            }
        }

        return output;
    }
}
