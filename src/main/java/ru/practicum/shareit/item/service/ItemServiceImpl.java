package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.reposotory.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposotory.CommentRepository;
import ru.practicum.shareit.item.reposotory.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.ITEM_NO_FOUND_FROM_ID;
import static ru.practicum.shareit.config.Constants.USER_NO_FOUND_FROM_ID;
import static ru.practicum.shareit.config.Constants.USER_NO_BOOKING;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoWithBookingDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + itemId));
        BookingInItemDtoResponse lastBooking = null;
        BookingInItemDtoResponse nextBooking = null;
        if (item.getOwner().getId() == userId) {
            lastBooking = bookingRepository.findLastBooking(itemId)
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            nextBooking = bookingRepository.findNextBooking(itemId)
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
        }
        Collection<CommentResponseDto> commentResponseDto = commentMapper
                .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
        return itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentResponseDto);
    }

    @Override
    public Collection<ItemDtoWithBookingDto> findByUserId(long userId) {
        Collection<ItemDtoWithBookingDto> itemDtoWithBookingDto = new ArrayList<>();
        for (Item item : itemRepository.findItemsByOwnerId(userId)) {
            BookingInItemDtoResponse lastBooking = bookingRepository.findLastBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            BookingInItemDtoResponse nextBooking = bookingRepository.findNextBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            Collection<CommentResponseDto> commentsResponseDto = commentMapper
                    .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
            itemDtoWithBookingDto.add(itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentsResponseDto));
        }
        return itemDtoWithBookingDto;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + userId));
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, user)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + itemDto.getId()));
        long itemOwnerId = item.getOwner().getId();
        if (itemOwnerId == userId) {
            Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
            Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
            Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
            return itemMapper.toItemDto(itemRepository.save(item));
        } else {
            throw new PermissionViolationException("Only owner can change item");
        }
    }

    @Transactional
    @Override
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {
        if (text.isEmpty()) return Collections.emptyList();
        return itemRepository.findItemsByName(text)
                .stream().map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(CommentDto commentDto, long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + itemId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
        Collection<Booking> completedBookings = bookingRepository
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(user, item, LocalDateTime.now(), BookingStatus.APPROVED);
        if (!completedBookings.isEmpty()) {
            Comment comment = commentMapper.toComment(commentDto, item, user);
            commentRepository.save(comment);
            return commentMapper.toCommentResponseDto(comment);
        } else {
            throw new BadRequestException(USER_NO_BOOKING);
        }
    }
}
