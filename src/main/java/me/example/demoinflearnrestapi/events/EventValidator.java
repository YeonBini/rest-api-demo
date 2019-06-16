package me.example.demoinflearnrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("BasePrice", "wrong value", "BasePrice is wrong");
            errors.rejectValue("MaxPrice", "wrong value", "MaxPrice is wrong");
        }

        LocalDateTime eventDateTime = eventDto.getEndEventDateTime();
        if(eventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
            eventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                eventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrong value", "endEventDateTime is wrong");
        }

        // ToDo BeginEventTime

        // ToDo CloseEnrollmentTime


    }
}
