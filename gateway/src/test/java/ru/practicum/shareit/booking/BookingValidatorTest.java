package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

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
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().minusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_EndDateBeforeStart_Error() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(2));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_StartAndEndDateAreSame_Error() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        LocalDateTime date = LocalDateTime.now();
        bookingRequestDto.setStart(date.plusDays(2));
        bookingRequestDto.setEnd(date.plusDays(2));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_StartNull_Error() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(null);
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void bookingValidator_EndNull_Error() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(2));
        bookingRequestDto.setEnd(null);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.size()).isEqualTo(1);
    }
}
