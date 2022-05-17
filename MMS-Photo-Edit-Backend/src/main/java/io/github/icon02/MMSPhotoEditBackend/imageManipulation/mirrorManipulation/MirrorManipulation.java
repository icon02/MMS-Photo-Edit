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

        return output;
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }
}
