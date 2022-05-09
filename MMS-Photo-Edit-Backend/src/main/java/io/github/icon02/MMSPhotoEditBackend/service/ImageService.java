package io.github.icon02.MMSPhotoEditBackend.service;

import io.github.icon02.MMSPhotoEditBackend.entity.ImageManipulationObject;
import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.mirrorManipulation.MirrorManipulation;
import io.github.icon02.MMSPhotoEditBackend.mapper.SelectionToRasterMapper;
import io.github.icon02.MMSPhotoEditBackend.repository.SessionTempRepository;
import io.github.icon02.MMSPhotoEditBackend.utils.MultipartImage;
import io.github.icon02.MMSPhotoEditBackend.imageManipulation.ImageResizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

@Service
public class ImageService {

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
        if(bimg == null) return false;

        ImageManipulationObject img = new ImageManipulationObject(imageFile.getOriginalFilename(), bimg);
        session.setImageManipulationObject(img);

        return true;
    }

    public MultipartImage getCurrent(String sessionId) {
        Session session = sessionRepository.get(sessionId);
        if(session == null) return null;

        ImageManipulationObject imgMo = session.getImageManipulationObject();
        if(imgMo == null) return null;

        return imageToFile(imgMo, imgMo.getCurrent());
    }


    public MultipartImage mirror(String sessionId, String direction, Object selection) {
        MirrorManipulation mirrorManipulation = new MirrorManipulation();
        switch (direction.toUpperCase(Locale.ROOT)) {
            case "VERTICAL": mirrorManipulation.setDirection(MirrorManipulation.Direction.VERTICAL); break;
            case "HORIZONTAL": mirrorManipulation.setDirection(MirrorManipulation.Direction.HORIZONTAL); break;
            default: return null;
        }

        ImageManipulationObject imgMo = get(sessionId);
        if(imgMo == null) return null;
        if(imgMo.getCurrent() == null) return null;

        Boolean[][] selectionRaster = prepareSelection(selection, imgMo.getCurrent());
        BufferedImage img = imgMo.applyFilter(mirrorManipulation, selectionRaster);

        return imageToFile(imgMo, img);
    }



    private ImageManipulationObject get(String sessionId) {
        if(sessionId == null) return null;
        Session session = sessionRepository.get(sessionId);
        if(session == null) return null;

        return session.getImageManipulationObject();
    }

    private Boolean[][] prepareSelection(Object selection, BufferedImage image) {
        if(selection == null) return null;
        if(image == null) return null;

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
        if(fileExtension == null) return null;

        if(img == null) return null;

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
}
