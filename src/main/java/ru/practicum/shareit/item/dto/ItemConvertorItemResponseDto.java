package ru.practicum.shareit.item.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingResponseFromGetItems;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.coment.dto.CommentConvertorCommentResponseDto;
import ru.practicum.shareit.coment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemConvertorItemResponseDto implements Converter<Item, ItemResponseDto> {

    CommentConvertorCommentResponseDto convertorCommentResponseDto = new CommentConvertorCommentResponseDto();

    @Override
    public ItemResponseDto convert(Item source) {
        return ItemResponseDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .build();
    }

    public GetItemResponseDTO convertFromUpdate(Item source, List<Comment> commentList) {
        return GetItemResponseDTO.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .comments(convertorCommentResponseDto.getListResponse(commentList))
                .build();
    }

    public GetItemResponseDTO convertToGetItemResponseDto(Item item, List<Booking> nextBooking, List<Booking> lastBooking, List<Comment> commentList) {
        GetItemResponseDTO itemResponseDTO = GetItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(convertorCommentResponseDto.getListResponse(commentList))
                .build();
        if (nextBooking.isEmpty()) {
            itemResponseDTO.setNextBooking(null);
        } else {
            itemResponseDTO.setNextBooking(BookingResponseFromGetItems.builder()
                    .id(nextBooking.get(0).getId())
                    .bookerId(nextBooking.get(0).getBooker().getId())
                    .build());
        }
        if (lastBooking.isEmpty()) {
            itemResponseDTO.setLastBooking(null);
        } else {
            itemResponseDTO.setLastBooking(BookingResponseFromGetItems.builder()
                    .id(lastBooking.get(0).getId())
                    .bookerId(lastBooking.get(0).getBooker().getId())
                    .build());
        }
        return itemResponseDTO;
    }

    public List<ItemResponseDto> getListResponse(List<Item> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
