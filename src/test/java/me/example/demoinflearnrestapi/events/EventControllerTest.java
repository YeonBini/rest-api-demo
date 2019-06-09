package me.example.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * WebMvcTest의 slice test에서는 Repository Bean 생성이 되어 있지 않아
     * 별도의 MockBean을 등록해주어야 한다.
     */
    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {

        // Given
        Event event = Event.builder()
                        .id(10)
                        .name("Spring")
                        .description("REST api lecture")
                        .beginEnrollmentDateTime(LocalDateTime.of(2019, 6, 9, 15, 16))
                        .closeEnrollmentDateTime(LocalDateTime.of(2019, 6, 10, 0, 0))
                        .beginEventDateTime(LocalDateTime.of(2019, 6, 9, 15, 16))
                        .endEventDateTime(LocalDateTime.of(2019, 6, 10, 0, 0))
                        .basePrice(100)
                        .maxPrice(200)
                        .limitOfEnrollment(100)
                        .location("왕십리역 삼부아파")
                        .build();

        Mockito.when(eventRepository.save(event))
                .thenReturn(event);

        // Then
        mockMvc.perform(post("/api/events")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .accept(MediaTypes.HAL_JSON)
                            .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE));
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"));
    }

}
