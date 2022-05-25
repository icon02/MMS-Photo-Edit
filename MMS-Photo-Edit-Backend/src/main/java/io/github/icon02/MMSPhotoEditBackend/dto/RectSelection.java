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
public class RectSelection extends CanvasSelection {
    private Point2D topLeft;
    private int width;
    private int height;
}
