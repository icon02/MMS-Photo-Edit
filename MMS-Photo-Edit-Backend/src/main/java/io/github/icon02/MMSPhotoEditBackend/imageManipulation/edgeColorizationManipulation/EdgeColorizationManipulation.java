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

    /**
     * Convolution matrices made for edge detection
     */
    private static final double[][] FILTER_SOBEL_V = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    private static final double[][] FILTER_SOBEL_H = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

    @Override
    public BufferedImage apply(BufferedImage image, Boolean[][] selectionRaster) {
        verifyState();

        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        double[][][] imageArray = transformImageToArray(image);
        //apply a vertical Edge Detection
        double[][] convolvedVerticalPixels = applyConvolution(image.getWidth(),
                image.getHeight(), imageArray, FILTER_SOBEL_V);
        //apply a horizontal Edge Detection
        double[][] convolvedHorizontalPixels = applyConvolution(image.getWidth(),
                image.getHeight(), imageArray, FILTER_SOBEL_H);

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (selectionRaster == null || selectionRaster[x][y] != null && selectionRaster[x][y]){
                    int value = Math.max(fixOutOfRangeRGBValues(convolvedVerticalPixels[x][y]),
                            fixOutOfRangeRGBValues(convolvedHorizontalPixels[x][y]));
                    // a certain threshold must be met to actually draw the edge onto the image
                    if (value > ((threshold+100)/(double)200)*255){
                        bi.setRGB(x, y, edgeColor.getRGB());
                    } else {
                        bi.setRGB(x, y, backgroundColor.getRGB());
                    }
                } else{
                    bi.setRGB(x, y,image.getRGB(x, y));
                }
            }
        }
        return bi;
    }

    /**
     * Turns Image into three separate 2-Dimensional arrays
     * (one for red, green and blue)
     * @param bufferedImage the image
     * @return array
     */
    private double[][][] transformImageToArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double[][][] image = new double[3][width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(bufferedImage.getRGB(x, y));
                image[0][x][y] = color.getRed();
                image[1][x][y] = color.getGreen();
                image[2][x][y] = color.getBlue();
            }
        }
        return image;
    }

    /**
     * applies Convolution for each color and then adds them back together
     * into one single 2-Dimensional Array
     * @param image the original image
     * @param filter a Convolution Matrix that will get applied to each color
     * @return the picture
     */
    private double[][] applyConvolution(int width, int height, double[][][] image, double[][] filter) {
        double[][] redConv = applyMatrix(width, height, image[0], filter);
        double[][] greenConv = applyMatrix(width, height, image[1], filter);
        double[][] blueConv = applyMatrix(width, height, image[2], filter);
        double[][] finalConv = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                finalConv[x][y] = redConv[x][y] + greenConv[x][y] + blueConv[x][y];
            }
        }
        return finalConv;
    }

    /**
     * applies Convolution Matrix to a 2-Dimensional array
     * @param matrix the convolution Matrix
     * @return the array
     */
    private double[][] applyMatrix(int width, int height, double[][] image, double[][] matrix) {
        double[][] result = new double[width][height];

        for (int x = 0; x < width - matrix.length+1; x++) {
            for (int y = 0; y < height - matrix[0].length+1; y++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        result[x][y] += image[x + i][y + j] * matrix[i][j];
                    }
                }
            }
        }
        return result;
    }

    private int fixOutOfRangeRGBValues(double value) {
        return ((int) (Math.abs(value) % 256));
    }

    private void verifyState() {
        if (this.threshold == null) throw new IllegalStateException("'threshold' must not be null");
        if (this.backgroundColor == null) throw new IllegalStateException("'backgroundColor' must not be null");
        if (this.edgeColor == null) throw new IllegalStateException("'edgeColor' must not be null");
    }
}
