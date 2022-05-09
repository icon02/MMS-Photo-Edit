package io.github.icon02.MMSPhotoEditBackend.filter;

import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import io.github.icon02.MMSPhotoEditBackend.entity.User;
import io.github.icon02.MMSPhotoEditBackend.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {

    public static final String ATTR_SESSION_ID_KEY = "session-id";

    private final SessionService sessionService;

    @Autowired
    public SessionFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (sessionId != null) {
            Session session = sessionService.getSession(sessionId);
            System.out.println();
            if (session != null && !session.isExpired()) {
                // set session-id so we can use it later in the controller
                request.setAttribute(ATTR_SESSION_ID_KEY, sessionId);

                // allow to reach endpoint
                User user = session.getUser();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }


}
