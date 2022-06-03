package io.github.icon02.MMSPhotoEditBackend.controller;

import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import io.github.icon02.MMSPhotoEditBackend.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/free")
    public ResponseEntity<Session> create() {
        Session session = sessionService.createFreeSession();

        return ResponseEntity.ok(session);
    }

    @PatchMapping("/update")
    public ResponseEntity<Session> update(HttpServletRequest request) {
        Session session = null;

        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/close")
    public ResponseEntity<Void> close(HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }
}
