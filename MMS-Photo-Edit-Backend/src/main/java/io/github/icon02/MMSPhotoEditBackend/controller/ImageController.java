package io.github.icon02.MMSPhotoEditBackend.controller;

import io.github.icon02.MMSPhotoEditBackend.filter.SessionFilter;
import io.github.icon02.MMSPhotoEditBackend.service.ImageService;
import io.github.icon02.MMSPhotoEditBackend.utils.MultipartImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    /* ==================== CONSTRUCTOR ==================== */

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /* ==================== GET MAPPINGS ==================== */

    @GetMapping("/current")
    public ResponseEntity<Resource> getCurrent(HttpServletRequest request) {
        String sessionId = getSessionId(request);

        MultipartImage image = imageService.getCurrent(sessionId);
        if (image == null) return ResponseEntity.badRequest().build();
        if (image.getOriginalFilename() == null) return ResponseEntity.internalServerError().build();

        HttpHeaders headers = prepareMultipartImageHeaders(image);

        return new ResponseEntity<>(image.getResource(), headers, HttpStatus.OK);
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

    /* ==================== PATCH MAPPINGS ==================== */
    // == image manipulation methods

    @PatchMapping("/mirror")
    public ResponseEntity<Resource> mirror(@RequestParam("dir") String direction, @RequestBody Object selection, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        MultipartImage image = imageService.mirror(sessionId, direction, selection);
        if(image == null) return ResponseEntity.badRequest().build();
        if(image.getOriginalFilename() == null) return ResponseEntity.internalServerError().build();

        HttpHeaders headers = prepareMultipartImageHeaders(image);
        return new ResponseEntity<>(image.getResource(), headers, HttpStatus.OK);
    }

    public ResponseEntity<Resource> rotate(@RequestParam("rotation") int rotation, HttpServletRequest request) {
        String sessionId = getSessionId(request);
        // TODO
        return null;
    }



    /* ==================== PRIVATE HELPER METHODS ==================== */
    private HttpHeaders prepareMultipartImageHeaders(MultipartImage image) {
        MediaType mediaType = MediaTypeFactory
                .getMediaType(image.getResource())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(image.getOriginalFilename())
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

}
