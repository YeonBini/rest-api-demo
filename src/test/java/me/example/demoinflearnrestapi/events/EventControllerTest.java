package me.example.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.example.demoinflearnrestapi.common.RestDocsConfiguration;
import me.example.demoinflearnrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    /**
     * WebMvcTest의 slice test에서는 Repository Bean 생성이 되어 있지 않아
     * 별도의 MockBean을 등록해주어야 한다.
     */
//    @MockBean
//    EventRepository eventRepository;
    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트 ")
    public void createEvent() throws Exception {

        // Given
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST api lecture")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 6, 9, 15, 16))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 6, 10, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 6, 9, 15, 16))
                .endEventDateTime(LocalDateTime.of(2019, 6, 10, 0, 0))
                .basePrice(100)
                .maxPrice(200)
                .location("왕십리역 삼부아파트")
                .build();

//        Mockito.when(eventRepository.save(event))
//                .thenReturn(event);

        // Then
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(10)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("link to query events"),
                                    linkWithRel("update-event").description("link to update existing event")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept headers"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type headers")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("Name of new Events"),
                                    fieldWithPath("description").description("description for event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("start time for enrollment"),
                                    fieldWithPath("closeEnrollmentDateTime").description("closing time for enrollment"),
                                    fieldWithPath("beginEventDateTime").description("time when event begins"),
                                    fieldWithPath("endEventDateTime").description("time when  event ends"),
                                    fieldWithPath("location").description("event location "),
                                    fieldWithPath("basePrice").description("min price of event"),
                                    fieldWithPath("maxPrice").description("max price of event"),
                                    fieldWithPath("limitOfEnrollment").description("limitation for enrollment")
                            ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("location for response header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content-tyhpe for response header")
                            ),
                            responseFields( // 문서의 일부분만 확인. link 정보는 상위에서 이미 정의 해둠
                                    fieldWithPath("id").description("id of new Events"),
                                    fieldWithPath("name").description("Name of new Events"),
                                    fieldWithPath("description").description("description for event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("start time for enrollment"),
                                    fieldWithPath("closeEnrollmentDateTime").description("closing time for enrollment"),
                                    fieldWithPath("beginEventDateTime").description("time when event begins"),
                                    fieldWithPath("endEventDateTime").description("time when  event ends"),
                                    fieldWithPath("location").description("event location "),
                                    fieldWithPath("basePrice").description("min price of event"),
                                    fieldWithPath("maxPrice").description("max price of event"),
                                    fieldWithPath("limitOfEnrollment").description("limitation for enrollment"),
                                    fieldWithPath("free").description("free or not"),
                                    fieldWithPath("offline").description("offline or online"),
                                    fieldWithPath("eventStatus").description("tells eventStatus"),
                                    fieldWithPath("_links.self.href").description("self link"),
                                    fieldWithPath("_links.query-events.href").description("query events link"),
                                    fieldWithPath("_links.update-event.href").description("update events link")
                            )
                        ))
        ;
    }

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @TestDescription("입력받을 수 없는 값을 받았을 때 에러가 발생하는 테스트 ")
    public void createEvent_Bad_Request() throws Exception {
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
                .location("왕십리역 삼부아파트")
                .free(true)
                .build();


        // Then
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("빈값이 들어왔을 때 에러가 발생하는 테스트 ")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력받은 값이 유효하지 않을 때 에러가 발생하는 테스트 ")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST api lecture")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 6, 10, 15, 16))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 6, 9, 0, 0))
                .beginEventDateTime(LocalDateTime.of(2019, 6, 10, 15, 16))
                .endEventDateTime(LocalDateTime.of(2019, 6, 9, 0, 0))
                .basePrice(300)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("왕십리역 삼부아파트")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
        ;
    }

}
