package io.github.icon02.MMSPhotoEditBackend.imageManipulation.rotateManipulation;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation.MirrorManipulation;
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
        TODO
        degrees do not have to be followed exactly. We only support
        90 right, 90 left und 180. Therefore, we only take the
        nearest value of 'degrees' that is supported
         */
        BufferedImage newImg = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (selectionRaster[i][j]) {
                    if (45 <= degrees && degrees <= 135)
                        newImg.setRGB((image.getHeight() - 1) - i, j, image.getRGB(j, i));
                    else if (135 < degrees && degrees <= 225)
                        new MirrorManipulation(MirrorManipulation.Direction.HORIZONTAL).apply(image, selectionRaster);
                    else if (225 < degrees && degrees <= 315)
                        newImg.setRGB(i, (image.getWidth() - 1) - j, image.getRGB(j, i));
                }
            }
        }
        return newImg;
    }

    private void verifyState() {
        if(degrees == null) throw new IllegalStateException("'degrees' must not be null");
    }
}
