package io.github.icon02.MMSPhotoEditBackend.imageManipulation.blurFilter;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlurFilter implements ImageFilter {

    private Integer variance;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        // TODO replace with custom implementation
        BufferedImage output = new BufferedImage(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);
        return output;
    }

    private void verifyState() {
        if(variance == null) throw new IllegalStateException("'variance' must not be null");
    }
}
