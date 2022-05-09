package io.github.icon02.MMSPhotoEditBackend.imageManipulation;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageResizeUtil {

    public static final String DIM_WIDTH = "width";
    public static final String DIM_HEIGHT = "height";

    private static final int thumbnail_max_area = 90_000;

    /**
     *
     * Resizes a given image to the expected size
     * Aspect Ratio might get lost
     *
     * @param original: original image
     * @param width: width of the generated image
     * @param height: height of the generated image
     * @return new Image with expected size
     */
    public BufferedImage resize(BufferedImage original, int width, int height) {
        BufferedImage output = new BufferedImage(width, height, original.getType());

        // TODO custom implementation

        return output;
    }

    public BufferedImage resize(BufferedImage original, int pixels, String dim) {

        // TODO custom implementation

        return null;
    }

    public BufferedImage createThumbnail(BufferedImage image) {
        /*
        TODO
        resize the image, so that the area of the new image
        is <= thumbnail_max_area but as large as possible
         */
        return new BufferedImage(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);
    }
}
