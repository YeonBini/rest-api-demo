package me.example.demoinflearnrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    // Code Convention
    // (prefix) parametersFor + Test할 method 명을 하면 자동으로 JUnitParamsRunner에서 인식한다.
    private Object[] parametersForTestFree() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{0, 100, false},
                new Object[]{100, 0, false},
                new Object[]{100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline() {
        return new Object[]{
                new Object[]{null, false},
                new Object[]{"", false},
                new Object[]{"성수역 스캘터랩스", true}
        };
    }

}