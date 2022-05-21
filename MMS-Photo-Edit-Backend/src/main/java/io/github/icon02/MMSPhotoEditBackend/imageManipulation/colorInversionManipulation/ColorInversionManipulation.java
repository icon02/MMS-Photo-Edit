package io.github.icon02.MMSPhotoEditBackend.imageManipulation.colorInversionManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorInversionManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {

        if (selectionRaster == null) // whole image
            return invertWholeImage(image);
        else // selection
            return invertWithSelection(image, selectionRaster);

    }


    private BufferedImage invertWholeImage(BufferedImage image) {

        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newImg.getWidth(); i++)
            for (int j = 0; j < newImg.getHeight(); j++) {

                Color c = new Color(image.getRGB(i, j));
                Color newColor = new Color(
                        255 - c.getRed(),
                        255 - c.getGreen(),
                        255 - c.getBlue()
                );
                newImg.setRGB(i, j, newColor.getRGB());

            }

        return newImg;
    }

    private BufferedImage invertWithSelection(BufferedImage image, Boolean[][] selectionRaster) {

        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newImg.getWidth(); i++)
            for (int j = 0; j < newImg.getHeight(); j++) {

                if (selectionRaster[i][j]) {
                    Color c = new Color(image.getRGB(i, j));
                    Color newColor = new Color(
                            255 - c.getRed(),
                            255 - c.getGreen(),
                            255 - c.getBlue()
                    );
                    newImg.setRGB(i, j, newColor.getRGB());

                }
            }

        return newImg;
    }
}
