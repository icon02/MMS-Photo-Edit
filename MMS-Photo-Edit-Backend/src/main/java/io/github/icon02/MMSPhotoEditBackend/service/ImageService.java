package io.github.icon02.MMSPhotoEditBackend.service;

import io.github.icon02.MMSPhotoEditBackend.entity.ImageManipulationObject;
import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.blurFilter.BlurFilter;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.brightnessManipulation.BrightnessManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.colorInversionManipulation.ColorInversionManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.edgeColorizationManipulation.EdgeColorizationManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.greyScaleManipulation.GreyScaleManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation.MirrorManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.rgbManipulation.RGBManipulation;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.rotateManipulation.RotateManipulation;
import io.github.icon02.MMSPhotoEditBackend.mapper.SelectionToRasterMapper;
import io.github.icon02.MMSPhotoEditBackend.repository.SessionTempRepository;
import io.github.icon02.MMSPhotoEditBackend.utils.MultipartImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

@Service
public class ImageService {

    public static final String PARAM_MIRROR_DIRECTION = "DIR";
    public static final String PARAM_ROTATION = "ROTATION";
    public static final String PARAM_RGB_R = "RED";
    public static final String PARAM_RGB_G = "GREEN";
    public static final String PARAM_RGB_B = "BLUE";

    public static final String PARAM_BRIGHTNESS_VAL = "VAL";

    public static final String PARAM_BLUR_VARIANCE = "VARIANCE";

    public static final String PARAM_EDGE_THRESHOLD = "THRESHOLD";
    public static final String PARAM_EDGE_BG_COLOR = "BG_COLOR";
    public static final String PARAM_EDGE_EDGE_COLOR = "EDGE_COLOR";



    private final SessionTempRepository sessionRepository;

    private final SelectionToRasterMapper selectionToRasterMapper;

    @Autowired
    public ImageService(SessionTempRepository sessionRepository, SelectionToRasterMapper selectionToRasterMapper) {
        this.sessionRepository = sessionRepository;
        this.selectionToRasterMapper = selectionToRasterMapper;
    }

    public boolean useImage(String sessionId, MultipartFile imageFile) {
        Session session = sessionRepository.get(sessionId);
        BufferedImage bimg = fileToImage(imageFile);
        if (bimg == null) return false;

        ImageManipulationObject img = new ImageManipulationObject(imageFile.getOriginalFilename(), bimg);
        session.setImageManipulationObject(img);

        return true;
    }

    public MultipartImage getCurrent(String sessionId) {
        Session session = sessionRepository.get(sessionId);
        if (session == null) return null;

        ImageManipulationObject imgMo = session.getImageManipulationObject();
        if (imgMo == null) return null;

        return imageToFile(imgMo, imgMo.getCurrent());
    }

    public MultipartImage manipulate(String sessionId, Object selection, ManipulationType type, HashMap<String, Object> params) {
        ImageManipulationObject imgMo = get(sessionId);
        if (imgMo == null) return null;
        if (imgMo.getCurrent() == null) return null;

        Boolean[][] selectionRaster = prepareSelection(selection, imgMo.getCurrent());
        ImageFilter filter = null;

        switch (type) {
            case MIRROR: {
                String dir = (String) params.get(PARAM_MIRROR_DIRECTION);
                switch (dir.toLowerCase(Locale.ROOT)) {
                    case "vertical":
                        filter = new MirrorManipulation(MirrorManipulation.Direction.VERTICAL);
                        break;
                    case "horizontal":
                        filter = new MirrorManipulation(MirrorManipulation.Direction.HORIZONTAL);
                        break;
                    default:
                        return null;
                }
                break;
            }
            case ROTATE: {
                Integer rotation = (Integer) params.get(PARAM_ROTATION);
                if (rotation == null) return null;
                filter = new RotateManipulation(rotation);
                break;
            }
            case RGB: {
                Integer r = (Integer) params.get(PARAM_RGB_R);
                Integer g = (Integer) params.get(PARAM_RGB_G);
                Integer b = (Integer) params.get(PARAM_RGB_B);
                filter = new RGBManipulation(r, g, b);
                break;
            }
            case GREYSCALE: {
                filter = new GreyScaleManipulation();
                break;
            }
            case BRIGHTNESS: {
                Integer val = (Integer) params.get(PARAM_BRIGHTNESS_VAL);
                filter = new BrightnessManipulation(BrightnessManipulation.Areas.ALL, val);
                break;
            }
            case DARK_BRIGHTNESS: {
                Integer val = (Integer) params.get(PARAM_BRIGHTNESS_VAL);
                filter = new BrightnessManipulation(BrightnessManipulation.Areas.DARK, val);
                break;
            }
            case BRIGHT_BRIGHTNESS: {
                Integer val = (Integer) params.get(PARAM_BRIGHTNESS_VAL);
                filter = new BrightnessManipulation(BrightnessManipulation.Areas.BRIGHT, val);
                break;
            }
            case BLUR: {
                Integer variance = (Integer) params.get(PARAM_BLUR_VARIANCE);
                filter = new BlurFilter(variance);
                break;
            }
            case COLOR_INVERT:
                filter = new ColorInversionManipulation();
                break;
            case EDGE_COLORIZATION:
                Integer threshold = (Integer) params.get(PARAM_EDGE_THRESHOLD);
                String bgColorStr = (String) params.get(PARAM_EDGE_BG_COLOR);
                Color bgColor = Color.decode(bgColorStr);
                String edgeColorStr = (String) params.get(PARAM_EDGE_EDGE_COLOR);
                Color edgeColor = Color.decode(edgeColorStr);

                filter = new EdgeColorizationManipulation(threshold, bgColor, edgeColor);
                break;
            default:
                return null;
        }

        if (filter == null) return null;
        BufferedImage updatedImage = imgMo.applyFilter(filter, selectionRaster);

        return imageToFile(imgMo, updatedImage);
    }


    @Deprecated
    public MultipartImage mirror(String sessionId, String direction, Object selection) {
        MirrorManipulation mirrorManipulation = new MirrorManipulation();
        switch (direction.toUpperCase(Locale.ROOT)) {
            case "VERTICAL":
                mirrorManipulation.setDirection(MirrorManipulation.Direction.VERTICAL);
                break;
            case "HORIZONTAL":
                mirrorManipulation.setDirection(MirrorManipulation.Direction.HORIZONTAL);
                break;
            default:
                return null;
        }

        ImageManipulationObject imgMo = get(sessionId);
        if (imgMo == null) return null;
        if (imgMo.getCurrent() == null) return null;

        Boolean[][] selectionRaster = prepareSelection(selection, imgMo.getCurrent());
        BufferedImage img = imgMo.applyFilter(mirrorManipulation, selectionRaster);

        return imageToFile(imgMo, img);
    }


    private ImageManipulationObject get(String sessionId) {
        if (sessionId == null) return null;
        Session session = sessionRepository.get(sessionId);
        if (session == null) return null;

        return session.getImageManipulationObject();
    }

    private Boolean[][] prepareSelection(Object selection, BufferedImage image) {
        if (selection == null) return null;
        if (image == null) return null;

        return selectionToRasterMapper.getRaster(selection, image.getWidth(), image.getHeight());
    }

    private BufferedImage fileToImage(MultipartFile file) {
        try {
            return ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MultipartImage imageToFile(ImageManipulationObject imgManObj, BufferedImage img) {
        String fileExtension = imgManObj.getFileExtension();
        if (fileExtension == null) return null;

        if (img == null) return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, fileExtension, baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new MultipartImage(
                baos.toByteArray(),
                "image",
                imgManObj.getFileName(),
                "image/" + fileExtension,
                baos.size()
        );
    }

    public enum ManipulationType {
        MIRROR,
        ROTATE,
        RGB,
        GREYSCALE,
        BRIGHTNESS,
        DARK_BRIGHTNESS,
        BRIGHT_BRIGHTNESS,
        BLUR,
        COLOR_INVERT,
        EDGE_COLORIZATION
    }
}
