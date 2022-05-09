package io.github.icon02.MMSPhotoEditBackend.imageManipulation.blurFilter;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;

import java.awt.image.BufferedImage;

public class BlurFilter implements ImageFilter {

    // parameter

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        // TODO replace with custom implementation
        BufferedImage output = new BufferedImage(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);
        return output;
    }
}
