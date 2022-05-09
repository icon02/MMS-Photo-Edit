package io.github.icon02.MMSPhotoEditBackend.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        response.addHeader("Access-Control-Allow-Origin", origin);
        response.addHeader("Access-Control-Allow-Headers", HttpHeaders.ORIGIN);
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.addHeader("Access-Control-Allow-Headers", HttpHeaders.CONTENT_TYPE);
        response.addHeader("Access-Control-Allow-Headers", HttpHeaders.ACCEPT);
        response.addHeader("Access-Control-Allow-Headers", HttpHeaders.AUTHORIZATION);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Allow-Methods", "PATCH");
        response.addHeader("Access-Control-Allow-Methods", "DELETE");
        response.addHeader("Access-Control-Allow-Methods", "OPTIONS");

        if("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // CORS preflight
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
