package io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
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
public class MirrorManipulation implements ImageFilter {

    private Direction direction;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        // TODO only for testing purpose, replace with custom implementation
        Color testColor = new Color(200, 50, 0);
        for(int y = 0; y < output.getHeight(); y++) {
            for(int x = 0; x < output.getWidth(); x++) {
                output.setRGB(x, y, testColor.getRGB());
            }
        }

        return output;
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}
