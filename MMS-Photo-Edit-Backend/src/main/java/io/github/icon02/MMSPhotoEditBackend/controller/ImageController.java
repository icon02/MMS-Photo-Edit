package io.github.icon02.MMSPhotoEditBackend.controller;

import io.github.icon02.MMSPhotoEditBackend.filter.SessionFilter;
import io.github.icon02.MMSPhotoEditBackend.mapper.HashMapToSelectionMapper;
import io.github.icon02.MMSPhotoEditBackend.service.ImageService;
import static io.github.icon02.MMSPhotoEditBackend.service.ImageService.ManipulationType.*;
import io.github.icon02.MMSPhotoEditBackend.utils.MultipartImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;
    private final HashMapToSelectionMapper selectionMapper;

    /* ==================== CONSTRUCTOR ==================== */

    @Autowired
    public ImageController(ImageService imageService, HashMapToSelectionMapper selectionMapper) {
        this.imageService = imageService;
        this.selectionMapper = selectionMapper;
    }

    /* ==================== GET MAPPINGS ==================== */

    @GetMapping("/current")
    public ResponseEntity<?> getCurrent(HttpServletRequest request) {
        String sessionId = getSessionId(request);

        MultipartImage image = imageService.getCurrent(sessionId);

        return buildImageResponse(image);
    }

    /* ==================== POST MAPPINGS ==================== */

    @PostMapping("/use")
    public ResponseEntity<Void> use(@RequestParam("image") MultipartFile imageFile, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        if (imageService.useImage(sessionId, imageFile))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    /* ===== IMAGE MANIPULATION ===== */

    @PostMapping("/mirror")
    public ResponseEntity<?> mirror(@RequestParam("dir") String direction, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_MIRROR_DIRECTION, direction);

        MultipartImage image =  imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), MIRROR, params);

        return buildImageResponse(image);
    }

    @PostMapping("/rotate")
    public ResponseEntity<?> rotate(@RequestParam("rotation") Integer rotation, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_ROTATION, rotation);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), ROTATE, params);

        return buildImageResponse(image);
    }

    @PostMapping("/rgb")
    public ResponseEntity<?> rgb(
            @RequestParam(defaultValue = "0") Integer r,
            @RequestParam(defaultValue = "0") Integer g,
            @RequestParam(defaultValue = "0") Integer b,
            @RequestBody Object selection,
            HttpServletRequest request) {
        //
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_RGB_R, r);
        params.put(ImageService.PARAM_RGB_G, g);
        params.put(ImageService.PARAM_RGB_B, b);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), RGB, params);

        return buildImageResponse(image);
    }

    @PostMapping("/greyscale")
    public ResponseEntity<?> greyscale(@RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), GREYSCALE, new HashMap<>());

        return buildImageResponse(image);
    }

    @PostMapping("/brightness")
    public ResponseEntity<?> generalBrightness(@RequestParam("val") Integer value, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_BRIGHTNESS_VAL, value);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), BRIGHTNESS, params);

        return buildImageResponse(image);
    }

    @PostMapping("/brightness/dark")
    public ResponseEntity<?> darkBrightness(@RequestParam("val") Integer value, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_BRIGHTNESS_VAL, value);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), DARK_BRIGHTNESS, params);

        return buildImageResponse(image);
    }

    @PostMapping("/brightness/bright")
    public ResponseEntity<?> brightBrightness(@RequestParam("val") Integer value, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_BRIGHTNESS_VAL, value);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), BRIGHT_BRIGHTNESS, params);

        return buildImageResponse(image);
    }

    @PostMapping("/blur")
    public ResponseEntity<?> blur(@RequestParam Integer variance, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        HashMap<String, Object> params = new HashMap<>();
        params.put(ImageService.PARAM_BLUR_VARIANCE, variance);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), BLUR, params);

        return buildImageResponse(image);
    }

    @PostMapping("/color-invert")
    public ResponseEntity<?> colorInvert(@RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        MultipartImage image = imageService.manipulate(sessionId, selectionMapper.toSelectionObject(selection), COLOR_INVERT, new HashMap<>());

        return buildImageResponse(image);
    }


    /* ==================== PRIVATE HELPER METHODS ==================== */
    private HttpHeaders prepareMultipartImageHeaders(MultipartImage image) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                // MediaTypeFactory
                // .getMediaType(image.getResource())
                // .orElse(MediaType.APPLICATION_OCTET_STREAM);

        String fileName = image.getOriginalFilename();
        if(fileName == null) fileName = "mms_edit";
        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(fileName)
                .build();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDisposition(disposition);
        /*
        see https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition
        CITE: "In a multipart/form-data body, the HTTP Content-Disposition general header
        is a header that must be used on each subpart of a multipart body to give information
        about the field it applies to. The subpart is delimited by the boundary defined in
        the Content-Type header. Used on the body itself, Content-Disposition has no effect."
         */
        headers.put("Access-Control-Expose-Headers", List.of("Content-Disposition"));

        return headers;
    }

    private String getSessionId(HttpServletRequest request) {
        return (String) request.getAttribute(SessionFilter.ATTR_SESSION_ID_KEY);
    }

    private ResponseEntity<?> buildImageResponse(MultipartImage image) {
        if(image == null) return ResponseEntity.badRequest().build();
        if(image.getOriginalFilename() == null) return ResponseEntity.internalServerError().build();

        HttpHeaders headers = prepareMultipartImageHeaders(image);
        return new ResponseEntity<>(image.getResource(), headers, HttpStatus.OK);
    }

}
