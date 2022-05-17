package io.github.icon02.MMSPhotoEditBackend.imageManipulation.blurFilter;

import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
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
//        BufferedImage output = new BufferedImage(image.getColorModel(), image.getRaster(), image.isAlphaPremultiplied(), null);
        int radius = (int)(2.5 * variance + 1);

        return blur(image, calcMatrix(radius), radius, selectionRaster);
    }

    private BufferedImage blur(BufferedImage image, double[][] weights, int radius, Boolean[][] selectionRaster) {

        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (selectionRaster[i][j]) {
                    // weighted colors
                    double[][] red   = new double[radius][radius];
                    double[][] green = new double[radius][radius];
                    double[][] blue  = new double[radius][radius];

                    // get surrounding pixels
                    for (int weightI = 0; weightI < weights.length; weightI++) {
                        for (int weightJ = 0; weightJ < weights[weightI].length; weightJ++) {

                            double currentWeight = weights[weightI][weightJ];

                            int sampleI = i + weightI - (weights.length / 2);
                            int sampleJ = j + weightJ - (weights.length / 2);

                            // if out of bounds
                            if (sampleI > (image.getWidth() -  1)) sampleI = image.getWidth() - 1 - radius;
                            if (sampleJ > (image.getHeight()-  1)) sampleJ = image.getHeight()- 1 - radius;

                            if (sampleI < 0) sampleI = Math.abs(sampleI);
                            if (sampleJ < 0) sampleJ = Math.abs(sampleJ);


                            Color color = new Color(image.getRGB(sampleI, sampleJ));

                            red  [weightI][weightJ] = currentWeight * color.getRed();
                            green[weightI][weightJ] = currentWeight * color.getGreen();
                            blue [weightI][weightJ]	= currentWeight * color.getBlue();
                        }
                    }
                    newImg.setRGB(i, j, new Color(weightedColor(red), weightedColor(green), weightedColor(blue)).getRGB());
                }
            }
        }
        return newImg;
    }

    private int weightedColor(double[][] weightedColor) {
        // we need to use a double for adding up the colors or we get a significant rounding error
        // and the image looses its brightness
        double s = 0;

        for (int i = 0; i < weightedColor.length; i++)
            for (int j = 0; j < weightedColor[i].length; j++)
                s += weightedColor[i][j];

        // convert to int
        int sum = (int)s;

        // if color is out of bounds in either direction set it to 255 or 0
        if 		(sum > 255) sum = 255;
        else if (sum < 0) 	sum =   0;

        return sum;
    }

    private double[][] calcMatrix(int radius) {
        double[][] weights = new double[radius][radius];
        double sum = 0;

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = gaussianModel(i - radius / 2, j - radius / 2);
                sum += weights[i][j];
            }
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++)
                weights[i][j] /= sum;
        }
        return weights;
    }

    private double gaussianModel(double x, double y) {
        return (1 / (2 * Math.PI * Math.pow(variance, 2))* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
    }


    private void verifyState() {
        if(variance == null) throw new IllegalStateException("'variance' must not be null");
    }
}
