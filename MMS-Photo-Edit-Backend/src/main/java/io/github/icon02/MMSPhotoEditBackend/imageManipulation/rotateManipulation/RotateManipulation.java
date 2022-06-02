package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rotateManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation.MirrorManipulation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RotateManipulation implements ImageFilter {

    private Integer degrees;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        return rotateImage(image , getActualDegrees(), selectionRaster);
    }


    /**
     * Rotate (part of) the image. Only rotation 90 degrees to the left or to the right
     * and 180 degrees rotation are supported.
     *
     * @param image             Original image.
     * @param deg               Degrees.
     * @param selectionRaster   Defines the part, that will be rotated, if not null;
     * @return                  Rotated image.
     */
    private BufferedImage rotateImage(BufferedImage image, int deg, Boolean[][] selectionRaster) {
        BufferedImage newImg;

        // 180 deg
        if (135 < deg && deg <= 225)
            return new MirrorManipulation(MirrorManipulation.Direction.HORIZONTAL).apply(image, selectionRaster);

        if (selectionRaster == null) { // whole image
            newImg = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    Color c = new Color(image.getRGB(j, i));
                    // 90 deg right
                    if (45 <= deg && deg <= 135)
                        newImg.setRGB((image.getHeight() - 1) - i, j, c.getRGB());
                    // 90 deg left
                    else if (225 < deg && deg <= 315)
                        newImg.setRGB(i, (image.getWidth() - 1) - j, c.getRGB());
                }
            }
        } else { // selection of the image
            newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            // fill new image
            for (int i = 0; i < image.getWidth(); i++)
                for (int j = 0; j < image.getHeight(); j++)
                    newImg.setRGB(i, j, image.getRGB(i, j));

            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    if (selectionRaster[j][i]) {
                        Color c = new Color(image.getRGB(j, i));
                        try {
                            // 90 deg right
                            if (45 <= deg && deg <= 135)
                                newImg.setRGB((image.getHeight() - 1) - i, j, c.getRGB());
                            // 90 deg left
                            else if (225 < deg && deg <= 315)
                                newImg.setRGB(i, (image.getHeight() - 1) - j, c.getRGB());
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    }
                }
            }
        }
        return newImg;
    }

    /**
     * Get the degrees as a value between 0 and 360.
     *
     * @return  Degrees.
     */
    private int getActualDegrees() {
        return degrees % 360;
    }

    private void verifyState() {
        if(degrees == null) throw new IllegalStateException("'degrees' must not be null");
    }
}