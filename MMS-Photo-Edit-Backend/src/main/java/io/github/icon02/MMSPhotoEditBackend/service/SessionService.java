package io.github.icon02.MMSPhotoEditBackend.service;

import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import io.github.icon02.MMSPhotoEditBackend.entity.User;
import io.github.icon02.MMSPhotoEditBackend.exception.NoSessionException;
import io.github.icon02.MMSPhotoEditBackend.repository.SessionTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {

    private static final int expiration_duration_min = 20;

    private final SessionTempRepository sessionRepository;

    private final User no_user = new User("", "");

    @Autowired
    public SessionService(SessionTempRepository sessionRepository)  {
        this.sessionRepository = sessionRepository;
    }

    public Session createFreeSession() {
        Session session = createSessionPrototype();
        session.setUser(no_user);

        scheduleExpiration(session);

        sessionRepository.save(session);

        return session;
    }

    public Session createSession(User user) {
        Session session = createSessionPrototype();
        session.setUser(user);

        sessionRepository.save(session);

        scheduleExpiration(session);

        return session;
    }

    /**
     * Lets the service know the session is still in use and
     * therefore session expiration will be delayed by defined
     * expiration duration.
     *
     * @param session: active session
     */
    public void useSession(Session session) {
        if(session == null) throw new NoSessionException("Cannot use null-session");

        LocalDateTime updated = LocalDateTime.now();
        LocalDateTime expires = updated.plusMinutes(expiration_duration_min);

        session.setUpdated(updated);
        session.setExpires(expires);
        sessionRepository.save(session);

        rescheduleExpiration(session);
    }

    public void closeSession(Session session) {

    }

    public Session getSession(String id) {
        return sessionRepository.get(id);
    }

    private void scheduleExpiration(Session session) {
        // initialize a thread that will run at expiration date
        // and removes the session with all its belongings from
        // the repository
    }

    private void rescheduleExpiration(Session session) {
        // clear current expiration thread

        scheduleExpiration(session);
    }

    private Session createSessionPrototype() {
        String id = sessionRepository.createId();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime expires = created.plusMinutes(expiration_duration_min);

        return new Session(id, created, null, expires, null, null);
    }
}
