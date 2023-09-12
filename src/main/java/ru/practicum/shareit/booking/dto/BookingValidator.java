package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingValidator implements ConstraintValidator<BookingValid, BookingDtoRequest> {
    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext context) {
        if (bookingDtoRequest.getStart() != null
                && bookingDtoRequest.getEnd() != null
                && (bookingDtoRequest.getStart().isBefore(LocalDateTime.now())
                || bookingDtoRequest.getEnd().isBefore(LocalDateTime.now())
                || bookingDtoRequest.getStart().isAfter(bookingDtoRequest.getEnd())
                || bookingDtoRequest.getStart().isEqual(bookingDtoRequest.getEnd()))
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Check end/start date")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
