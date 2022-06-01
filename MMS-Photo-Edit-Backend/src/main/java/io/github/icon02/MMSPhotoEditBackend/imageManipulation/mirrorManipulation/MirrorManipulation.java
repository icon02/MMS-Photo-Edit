package io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
import java.awt.image.BufferedImage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MirrorManipulation implements ImageFilter {

    private Direction direction;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        return mirrorImage(image, selectionRaster);
    }


    /**
     * Mirror (part of) the image.
     *
     * @param image           Original image.
     * @param selectionRaster Defines the part, that will be mirrored, if not null;
     * @return Mirrored image.
     */
    private BufferedImage mirrorImage(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newImg.getHeight(); i++) {
            for (int j = 0; j < newImg.getWidth(); j++) {

                Color c = new Color(image.getRGB(j, i));
                if (selectionRaster == null ||
                        selectionRaster.length <= i || selectionRaster[i].length <= j ||
                        selectionRaster[i][j] != null && selectionRaster[i][j]) {
                    //
                    if (direction == Direction.VERTICAL)
                        newImg.setRGB((newImg.getWidth() - 1) - j, i, c.getRGB());
                    else if (direction == Direction.HORIZONTAL)
                        newImg.setRGB(j, (newImg.getHeight() - 1) - i, c.getRGB());
                } else newImg.setRGB(i, j, c.getRGB());

            }
        }
        return newImg;
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}