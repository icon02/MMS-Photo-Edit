package io.github.icon02.MMSPhotoEditBackend.imageManipulation.edgeColorizationManipulation;

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
public class EdgeColorizationManipulation implements ImageFilter {

    /**
     * Threshold: <br>
     * percentage value from -100 to 100 <br>
     * defines how much of a color difference
     * is needed to 'detect' an edge
     */
    private Integer threshold;
    private Color backgroundColor;
    private Color edgeColor;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        // TODO
        return null;
    }

    private void verifyState() {
        if(this.threshold == null) throw new IllegalStateException("'threshold' must not be null");
        if(this.backgroundColor == null) throw new IllegalStateException("'backgroundColor' must not be null");
        if(this.edgeColor == null) throw new IllegalStateException("'edgeColor' must not be null");
    }
}
