package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2023-06-14T20:27:59+0300",
        comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {
    @Override
    public ItemRequest mapToBooking(ItemRequestRequestDto dto) {
        if (dto == null) {
            return null;
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(dto.getId());
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(dto.getCreated());
        itemRequest.setRequestor(dto.getRequestor());
        return null;
    }
}