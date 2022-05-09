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
public class RectSelection {
    private Point2D topLeft;
    private int width;
    private int height;

    public RectSelection(int topLeftX, int topLeftY, int width, int height) {
        this(new Point2D(topLeftX, topLeftY), width, height);
    }
}
