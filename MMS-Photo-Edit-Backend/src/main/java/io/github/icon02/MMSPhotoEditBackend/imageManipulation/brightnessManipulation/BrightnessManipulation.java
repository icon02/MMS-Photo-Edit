package io.github.icon02.MMSPhotoEditBackend.imageManipulation.brightnessManipulation;

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
public class BrightnessManipulation implements ImageFilter {

    private Areas area;
    private Integer value;


    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        // TODO
        return null;
    }

    private void verifyState() {
        if(area == null) throw new IllegalStateException("'area' must not be null");
    }

    public enum Areas {
        ALL, BRIGHT, DARK
    }
}
