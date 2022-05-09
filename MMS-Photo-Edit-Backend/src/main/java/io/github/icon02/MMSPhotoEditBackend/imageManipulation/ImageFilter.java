package io.github.icon02.MMSPhotoEditBackend.imageManipulation;

import java.awt.image.BufferedImage;

public interface ImageFilter {
    BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster);
}
