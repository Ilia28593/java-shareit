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
    public void bookingValidator_StartDateBeforeNow_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().minusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_EndDateBeforeStart_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(2));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_StartAndEndDateAreSame_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        LocalDateTime date = LocalDateTime.now();
        bookingDtoRequest.setStart(date.plusDays(2));
        bookingDtoRequest.setEnd(date.plusDays(2));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_StartNull_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(null);
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_EndNull_Error() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(2));
        bookingDtoRequest.setEnd(null);
        Set<ConstraintViolation<BookingDtoRequest>> violations = validator.validate(bookingDtoRequest);
        assertThat(violations.size()).isEqualTo(1);
    }
}
