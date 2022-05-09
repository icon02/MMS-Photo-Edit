package io.github.icon02.MMSPhotoEditBackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.icon02.MMSPhotoEditBackend.entity.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerMvcTests {

    private static final String base_uri = "/sessions";
    final int min_session_dur = 10;

    private ObjectMapper objectMapper;

    public SessionControllerMvcTests() {
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Autowired
    private MockMvc mockMvc;

    /*
    @Test
    public void testCreateSessionSimple() throws Exception {
        MvcResult result = mockMvc.perform(post(base_uri + "/create"))
                .andExpect(status().is(200))
                .andReturn();

        String sessionJson = result.getResponse().getContentAsString();
        Session session = objectMapper.readValue(sessionJson, Session.class);

        assertNotNull(session.getId());
        assertNotNull(session.getCreated());
        assertNull(session.getUpdated());
        assertNotNull(session.getExpires());

        LocalDateTime minExpires = LocalDateTime.now().plusMinutes(min_session_dur);
        assertTrue(minExpires.isBefore(session.getExpires()));
    }

    // @Test
    public void testUpdateSessionSimple() throws Exception {
        MvcResult createResult = mockMvc.perform(post(base_uri + "/create"))
                .andReturn();

        String sessionJson = createResult.getResponse().getContentAsString();
        Session createdSession = objectMapper.readValue(sessionJson, Session.class);

        assertNotNull(createdSession);
        assertNotNull(createdSession.getId());

        Thread.sleep(500);
        LocalDateTime beforeUpdate = LocalDateTime.now();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.put(HttpHeaders.ACCEPT_PATCH, Collections.singletonList(createdSession.getId()));
        MvcResult updateResult = mockMvc.perform(
                        patch(base_uri + "/update")
                                .header("test", "test")
                )
                .andReturn();
        LocalDateTime afterUpdate = LocalDateTime.now();

        sessionJson = updateResult.getResponse().getContentAsString();
        Session updatedSession = objectMapper.readValue(sessionJson, Session.class);

        LocalDateTime minExpires = LocalDateTime.now().plusMinutes(min_session_dur);

        assertTrue(minExpires.isBefore(updatedSession.getExpires()));
        assertNotNull(updatedSession.getUpdated());
        assertTrue(updatedSession.getExpires().isAfter(createdSession.getExpires()));
        assertTrue(updatedSession.getCreated().isEqual(createdSession.getCreated()));
        assertTrue(
                updatedSession.getUpdated().isAfter(beforeUpdate)
                && updatedSession.getUpdated().isBefore(afterUpdate)
        );

    }

     */

}
