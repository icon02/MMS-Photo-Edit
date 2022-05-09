package io.github.icon02.MMSPhotoEditBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageResizeUtil;
import lombok.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageManipulationObject {

    @JsonIgnore
    @Getter(AccessLevel.PRIVATE)
    private final ImageResizeUtil imageResizeUtil = new ImageResizeUtil();

    private String fileName;
    private ArrayList<BufferedImage> imageHistory = new ArrayList<>();
    private int currentIndex = -1;
    private BufferedImage thumbnail;
    @JsonIgnore
    private boolean locked = false;

    public ImageManipulationObject(String fileName, BufferedImage baseImage) {
        this.fileName = fileName;
        this.imageHistory.add(baseImage);
        this.currentIndex = 0;

        this.thumbnail = imageResizeUtil.createThumbnail(baseImage);
    }

    public BufferedImage getCurrent() {
        if(currentIndex >= 0) return imageHistory.get(currentIndex);
        else return null;
    }

    public String getFileExtension() {
        if(fileName == null || fileName.length() == 0) return null;

        StringBuilder builder = new StringBuilder();
        for(int idx = fileName.length() - 1; idx >= 0 && fileName.charAt(idx) != '.'; idx--) {
            builder.append(fileName.charAt(idx));
        }

        builder.reverse();
        return builder.toString();
    }

    /**
     * Applies the filter to the current image.
     *
     * Overrides possible redos.
     *
     * @param filter: filter to apply
     * @return the BufferedImage that was created
     */
    public BufferedImage applyFilter(ImageFilter filter, Boolean[][] selectionRaster) {
        if(currentIndex < 0) return null;

        BufferedImage withFilter = filter.apply(imageHistory.get(currentIndex), selectionRaster);
        if(currentIndex >= imageHistory.size()) removeRedos();

        imageHistory.add(withFilter);
        currentIndex++;

        updateThumbnail();

        return withFilter;
    }

    /**
     * Applies given filter to the current thumbnail and returns
     * the image
     *
     * Calling this method takes no effect on the current state
     *
     * @param filter: filter to apply
     * @return the current Thumbnail with the applied filter
     */
    public BufferedImage applyPreviewFilter(ImageFilter filter) {
        if(thumbnail == null) return null;
        
        BufferedImage preview = filter.apply(thumbnail, null);
        return preview;
    }

    public boolean undo() {
        if(currentIndex <= 0) return false;

        currentIndex--;
        updateThumbnail();

        return true;
    }

    public boolean redo() {
        if(currentIndex < 0) return false;
        if(currentIndex >= imageHistory.size()) return false;

        currentIndex++;
        updateThumbnail();

        return true;
    }

    private void removeRedos() {
        for(int i = 0; i < imageHistory.size() - currentIndex; i++)
            imageHistory.remove(imageHistory.size() - 1);
    }

    private void updateThumbnail() {
        if(currentIndex < 0) return;
        if(currentIndex >= imageHistory.size()) return;

        thumbnail = imageResizeUtil.createThumbnail(imageHistory.get(currentIndex));
    }

}
