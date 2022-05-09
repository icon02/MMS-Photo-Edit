package io.github.icon02.MMSPhotoEditBackend.dto;

import io.github.icon02.MMSPhotoEditBackend.utils.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CircleSelection {
    private Point2D center;
    private int radius;

    public CircleSelection(int centerX, int centerY, int radius) {
        this(new Point2D(centerX, centerY), radius);
    }
}
