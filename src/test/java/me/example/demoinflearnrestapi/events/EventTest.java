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

    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isTrue();// Given

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline() {
        // Given
        Event event = Event.builder()
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();

        // Given
        event = Event.builder()
                .location("성수역 스캘터랩스")
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();

    }

}