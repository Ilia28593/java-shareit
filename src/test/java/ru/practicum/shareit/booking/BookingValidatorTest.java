package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookingValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void startDateBeforeNow_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest()
                .setItemId(1L)
                .setStart(LocalDateTime.now().minusDays(1))
                .setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void endDateBeforeStart_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest()
                .setItemId(1L)
                .setStart(LocalDateTime.now().plusDays(2))
                .setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void startAndEndDateAreSame_Error() {
        LocalDateTime date = LocalDateTime.now();
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest()
                .setItemId(1L)
                .setStart(date.plusDays(2))
                .setEnd(date.plusDays(2));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void bookingValidator_StartNull_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest()
                .setItemId(1L)
                .setStart(null)
                .setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void bookingValidator_EndNull_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest()
                .setItemId(1L)
                .setStart(LocalDateTime.now().plusDays(2))
                .setEnd(null);
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }
}
