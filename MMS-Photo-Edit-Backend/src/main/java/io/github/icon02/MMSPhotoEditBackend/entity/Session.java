package io.github.icon02.MMSPhotoEditBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private String id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalDateTime expires;
    private User user;
    private ImageManipulationObject imageManipulationObject;

    public boolean isExpired() {
        return expires.isBefore(LocalDateTime.now());
    }
}
