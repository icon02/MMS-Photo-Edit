package io.github.icon02.MMSPhotoEditBackend.imageManipulation.colorInversionManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorInversionManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        return invertImage(image, selectionRaster);
    }


    /**
     * Inverts the color of (the selected part of) the image.
     *
     * @param image             Original image.
     * @param selectionRaster   Defines the part, that will be color-inverted, if not null;
     * @return                  Inverted image.
     */
    private BufferedImage invertImage(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newImg.getWidth(); i++) {
            for (int j = 0; j < newImg.getHeight(); j++) {
                Color c = new Color(image.getRGB(i, j));
                if (selectionRaster == null || selectionRaster[i][j] != null && selectionRaster[i][j]) {
                    Color newColor = new Color(
                            255 - c.getRed(),
                            255 - c.getGreen(),
                            255 - c.getBlue()
                    );
                newImg.setRGB(i, j, newColor.getRGB());
                }
                else newImg.setRGB(i, j, c.getRGB());
            }
        }
        return newImg;
    }
}