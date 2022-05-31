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

    // used to calculate the weights in the weight matrix
    private Integer variance;

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();
        int radius = (int)(2.5 * variance + 1);
        double[][] matrix = calcMatrix(radius);

        return blurImage(image, matrix, radius, selectionRaster);
    }


    /**
     * Applies Gaussian Blur (on a selected part of) the image.
     *
     * @param image             Original image.
     * @param weights           Weight matrix.
     * @param radius            Radius of matrices for R, G and B values, on which the weight matrix will be applied onto.
     * @param selectionRaster   Defines the part, that will be blurred, if not null;
     * @return                  Blurred image.
     */
    private BufferedImage blurImage(BufferedImage image, double[][] weights, int radius, Boolean[][] selectionRaster) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                if (selectionRaster == null || selectionRaster[i][j] != null && selectionRaster[i][j]) {
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
                            if (sampleI > (image.getWidth() - 1)) sampleI  = image.getWidth() - 1 - radius;
                            if (sampleJ > (image.getHeight() - 1)) sampleJ = image.getHeight() - 1 - radius;
                            if (sampleI < 0) sampleI = Math.abs(sampleI);
                            if (sampleJ < 0) sampleJ = Math.abs(sampleJ);


                            Color color = new Color(image.getRGB(sampleI, sampleJ));

                            red   [weightI][weightJ] = currentWeight * color.getRed();
                            green [weightI][weightJ] = currentWeight * color.getGreen();
                            blue  [weightI][weightJ] = currentWeight * color.getBlue();

                        } // end for4
                    } // end for3
                    newImg.setRGB(i, j, new Color(weightedColor(red), weightedColor(green), weightedColor(blue)).getRGB());
                } // end if
                else newImg.setRGB(i, j, image.getRGB(i, j));

            } // end for2
        } // end for1
        return newImg;
    }

    /**
     * Calculates the new Color value for a pixel.
     *
     * @param weightedColor     Weight matrix consisting of either R, G or B values
     * @return                  New weighted Color.
     */
    private int weightedColor(double[][] weightedColor) {
        double s = 0;

        for (int i = 0; i < weightedColor.length; i++)
            for (int j = 0; j < weightedColor[i].length; j++)
                s += weightedColor[i][j];

        int sum = (int)s;

        // if color is out of bounds in either direction set it to 255 or 0
        if 		(sum > 255) sum = 255;
        else if (sum < 0) 	sum =   0;

        return sum;
    }

    /**
     * Calculate the full weight matrix.
     *
     * @param radius    Defines the size of the matrix.
     * @return          Weight matrix.
     */
    private double[][] calcMatrix(int radius) {
        double[][] weights = new double[radius][radius];
        double sum = 0;

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = gaussianFormula(i - radius / 2.0, j - radius / 2.0);
                sum += weights[i][j];
            }
        }
        for (int i = 0; i < weights.length; i++)
            for (int j = 0; j < weights[i].length; j++)
                weights[i][j] /= sum;

        return weights;
    }

    /**
     * The Gaussian Formula to calculate individual weights in the weight matrix.
     *
     * @param i     Row index.
     * @param j     Column index.
     * @return      Weight at index i,j.
     */
    private double gaussianFormula(double i, double j) {
        return (1 / (2*Math.PI*Math.pow(variance, 2)) * Math.exp(-(Math.pow(i, 2)+Math.pow(j, 2)) / (2*Math.pow(variance, 2))));
    }

    private void verifyState() {
        if(variance == null) throw new IllegalStateException("'variance' must not be null");
    }
}