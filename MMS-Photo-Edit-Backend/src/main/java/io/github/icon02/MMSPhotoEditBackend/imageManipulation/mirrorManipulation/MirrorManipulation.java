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
<<<<<<< HEAD
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < newImg.getHeight(); i++ ) {
            for (int j = 0; j < newImg.getWidth(); j++) {
                if (selectionRaster[i][j]) {
                    Color c = new Color(image.getRGB(j, i));
                    if (direction == Direction.VERTICAL)
                         newImg.setRGB((newImg.getWidth() - 1) - j, i, c.getRGB());
                    else newImg.setRGB(j,(newImg.getHeight() - 1) - i, c.getRGB());
                }
            }
        }
        return newImg;
=======
        return null;
>>>>>>> Master
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}
