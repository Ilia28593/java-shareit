package ru.practicum.shareit.coment.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.coment.model.Comment;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-06-14T20:27:59+0300",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment mapToComment(CommentRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return Comment.builder()
                .text(dto.getText())
                .created(LocalDateTime.now())
                .build();
    }
}

