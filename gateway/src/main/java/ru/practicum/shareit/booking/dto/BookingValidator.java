package ru.practicum.shareit.booking.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingValidator implements ConstraintValidator<BookingValid, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto bookingRequestDto, ConstraintValidatorContext context) {
        if (bookingRequestDto.getStart() != null
                && bookingRequestDto.getEnd() != null
                && (bookingRequestDto.getStart().isBefore(LocalDateTime.now())
                || bookingRequestDto.getEnd().isBefore(LocalDateTime.now())
                || bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd())
                || bookingRequestDto.getStart().isEqual(bookingRequestDto.getEnd()))
        ) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Check end/start date")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
