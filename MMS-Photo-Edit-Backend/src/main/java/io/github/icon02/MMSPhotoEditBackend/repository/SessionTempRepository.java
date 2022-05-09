package io.github.icon02.MMSPhotoEditBackend.repository;

import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class SessionTempRepository {

    private HashMap<String, Session> sessions = new HashMap<>();

    public void save(Session session) {
        if(session.getId() == null) throw new NullPointerException("session.id must not be null");

        sessions.put(session.getId(), session);
    }

    public Session get(String id) {
        if(id == null) return null;

        return sessions.get(id);
    }

    public Session delete(String id) {
        return sessions.remove(id);
    }

    public String createId() {
        String id = null;
        while(id == null || sessions.containsKey(id))
            id = UUID.randomUUID().toString();

        return id;
    }
}
