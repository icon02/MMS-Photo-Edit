package io.github.icon02.MMSPhotoEditBackend.imageManipulation.colorInversionManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorInversionManipulation implements ImageFilter {

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

       // TODO

        return output;
    }
}
