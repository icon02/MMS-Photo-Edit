package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rgbManipulation;

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
public class RGBManipulation implements ImageFilter {
    // values in percentage
    private Integer rDif = null; // -100 <= rDif <= 100
    private Integer gDif = null; // -100 <= gDif <= 100
    private Integer bDif = null; // -100 <= bDif <= 100

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        BufferedImage output = new BufferedImage(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);
        // TODO
        return output;
    }

    private void verifyState() {
        if(rDif == null) throw new IllegalStateException("'rDif' must not be null");
        if(rDif < -100 || rDif > 100) throw new IllegalStateException("'rDif' must be between -100 and 100");

        if(gDif == null) throw new IllegalStateException("'gDif' must not be null");
        if(gDif < -100 || gDif > 100) throw new IllegalStateException("'gDif' must be between -100 and 100");

        if(bDif == null) throw new IllegalStateException("'bDif' must not be null");
        if(bDif < -100 || bDif > 100) throw new IllegalStateException("'bDif' must be between -100 and 100");
    }
}
