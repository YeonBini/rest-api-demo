package me.example.demoinflearnrestapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {


    @Test
    public void builder() {
        Event event = Event.builder()
                        .name("inflearn")
                        .description("REST API lecture")
                        .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javabean() {
        // Given
        final String inflearn = "Inflearn";
        final String description = "Spring";

        // When
        Event event = new Event();
        event.setName(inflearn);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(inflearn);
        assertThat(event.getDescription()).isEqualTo(description);

    }

}