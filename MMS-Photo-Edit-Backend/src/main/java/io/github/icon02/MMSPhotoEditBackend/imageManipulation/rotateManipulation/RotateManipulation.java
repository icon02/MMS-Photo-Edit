package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rotateManipulation;

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
public class RotateManipulation implements ImageFilter {

    private Integer degrees;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        /*
        degrees do not have to be followed exactly. We only support
        90 right, 90 left und 180. Therefore, we only take the
        nearest value of 'degrees' that is supported
         */
        return null;
    }

    private void verifyState() {
        if(degrees == null) throw new IllegalStateException("'degrees' must not be null");
    }
}
