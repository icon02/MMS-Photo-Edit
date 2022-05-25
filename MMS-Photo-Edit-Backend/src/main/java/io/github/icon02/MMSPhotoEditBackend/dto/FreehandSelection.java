package io.github.icon02.MMSPhotoEditBackend.dto;

import io.github.icon02.MMSPhotoEditBackend.utils.Point2D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FreehandSelection {
    private List<Point2D> points;
}
