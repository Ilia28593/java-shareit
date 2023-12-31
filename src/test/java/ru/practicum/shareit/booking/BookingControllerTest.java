package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.config.Constants;

import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private final BookingService bookingService;
    private final BookingDtoRequest bookingDtoRequest = Samples.getBooking(1);
    private final BookingDtoResponse bookingDtoResponse = Samples.getBookingResponse(1, 1L);

    @Test
    public void create() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.booker", is(bookingDtoResponse.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingDtoResponse.getItem()), ItemDto.class));
    }

    @Test
    public void approve() throws Exception {
        when(bookingService.approve(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/{bookingId}?approved=true", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.booker", is(bookingDtoResponse.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingDtoResponse.getItem()), ItemDto.class));
    }

    @Test
    public void getById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().format(ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.booker", is(bookingDtoResponse.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.item", is(bookingDtoResponse.getItem()), ItemDto.class));
    }

    @Test
    public void getByIdIsNotFound() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getByIdIsBadRequest() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenThrow(BadRequestException.class);
        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdInsteadOfIntIsBadRequest() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenThrow(BadRequestException.class);

        mvc.perform(get("/bookings/{bookingId}", "NOT_ID")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllByBookerId() throws Exception {
        when(bookingService.getAllByBookerId(any(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse, bookingDtoResponse));
        mvc.perform(get("/bookings?state=All", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void getAllByBookerIdBadRequest() throws Exception {
        when(bookingService.getAllByBookerId(any(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse, bookingDtoResponse));
        mvc.perform(get("/bookings?state=UNKNOWN-STATE", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllByItemOwnerId() throws Exception {
        when(bookingService.getAllByItemOwnerId(any(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse, bookingDtoResponse));

        mvc.perform(get("/bookings/owner?state=All", 1L)
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }
}
