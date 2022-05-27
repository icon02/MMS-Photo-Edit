package io.github.icon02.MMSPhotoEditBackend.imageManipulation;

import java.awt.*;

public class ImageManipulationUtil {

    /**
     * Adds or subtracts a certain percentage to/from each color
     * if percentage is 100 the colorValue will be 255 for example, if it is -100 it will be 0
     * for 0 percent it will just stay the same value
     * @param c The Color
     * @return the new Color with the percents applied
     */
    public static int applyPercentageColor(Color c, int redPercent, int greenPercent, int bluePercent){
        return new Color(applyPercentageInt(c.getRed(), redPercent),
                applyPercentageInt(c.getGreen(), greenPercent),
                applyPercentageInt(c.getBlue(), bluePercent)).getRGB();
    }

    private static int applyPercentageInt(int value, int percentage){
        if (percentage > 0){
            int difference = 255 - value;
            return value + difference * percentage / 100;
        } else {
            return value + value * percentage / 100;
        }
    }
}
