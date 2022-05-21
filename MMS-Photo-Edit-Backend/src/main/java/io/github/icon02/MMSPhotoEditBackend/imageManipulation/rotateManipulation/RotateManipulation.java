package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rotateManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation.MirrorManipulation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RotateManipulation implements ImageFilter {

    private Integer degrees;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();

        int deg = getActualDegrees();

        if (selectionRaster == null) // whole image
            return rotateWholeImage(image , deg);
        else // selection
            return rotateWithSelection(image, selectionRaster , deg);

    }


    private BufferedImage rotateWholeImage(BufferedImage image, int deg) {

        BufferedImage newImg = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB);

        // 180 deg
        if (135 < deg && deg <= 225)
            return new MirrorManipulation(MirrorManipulation.Direction.HORIZONTAL).apply(image, null);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {

                // 90 deg right
                if (45 <= deg && deg <= 135)
                    newImg.setRGB((image.getHeight() - 1) - i, j, image.getRGB(j, i));

                // 90 deg left
                else if (225 < deg && deg <= 315)
                    newImg.setRGB(i, (image.getWidth() - 1) - j, image.getRGB(j, i));
            }
        }

        return newImg;
    }

    private BufferedImage rotateWithSelection(BufferedImage image, Boolean[][] selectionRaster, int deg) {

        BufferedImage newImg = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB);

        // 180 deg
        if (135 < deg && deg <= 225)
            return new MirrorManipulation(MirrorManipulation.Direction.HORIZONTAL).apply(image, selectionRaster);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (selectionRaster[i][j]) {

                    // 90 deg right
                    if (45 <= deg && deg <= 135)
                        newImg.setRGB((image.getHeight() - 1) - i, j, image.getRGB(j, i));

                    // 90 deg left
                    else if (225 < deg && deg <= 315)
                        newImg.setRGB(i, (image.getWidth() - 1) - j, image.getRGB(j, i));
                }
            }
        }

        return newImg;
    }

    private int getActualDegrees() {
        return this.degrees % 360;
    }

    private void verifyState() {
        if(degrees == null) throw new IllegalStateException("'degrees' must not be null");
    }
}
