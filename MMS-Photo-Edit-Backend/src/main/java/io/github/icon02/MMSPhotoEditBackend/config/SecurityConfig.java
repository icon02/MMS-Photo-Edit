package io.github.icon02.MMSPhotoEditBackend.config;

import io.github.icon02.MMSPhotoEditBackend.filter.CorsFilter;
import io.github.icon02.MMSPhotoEditBackend.filter.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SessionFilter sessionFilter;
    private CorsFilter corsFilter;

    @Autowired
    public SecurityConfig(SessionFilter sessionFilter, CorsFilter corsFilter) {
        this.sessionFilter = sessionFilter; this.corsFilter = corsFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "**").permitAll() // required for CORS-preflight
                .antMatchers("/sessions/free", HttpMethod.POST.name()).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(corsFilter, SessionFilter.class);
    }

}
