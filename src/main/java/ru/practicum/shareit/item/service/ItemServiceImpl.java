package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInItemDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.reposotory.BookingRepository;
import ru.practicum.shareit.config.Utilities;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposotory.CommentRepository;
import ru.practicum.shareit.item.reposotory.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.ITEM_NO_FOUND_FROM_ID;
import static ru.practicum.shareit.config.Constants.USER_NO_BOOKING;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestServiceImpl requestService;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoInBookingDto getById(long itemId, long userId) {
        Item item = findById(itemId);
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
    public Collection<ItemDtoInBookingDto> findByUserId(long userId, int from, int size) {
        Collection<ItemDtoInBookingDto> itemDtoByBookingDtos = new ArrayList<>();
        Collection<Item> items;
        Pageable pageable = Utilities.getPageable(from, size, Sort.unsorted());
        items = itemRepository.findItemsByOwnerId(userId, pageable);
        for (Item item : items) {
            BookingInItemDtoResponse lastBooking = bookingRepository.findLastBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            BookingInItemDtoResponse nextBooking = bookingRepository.findNextBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            Collection<CommentResponseDto> commentsResponseDto = commentMapper
                    .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
            itemDtoByBookingDtos.add(itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentsResponseDto));
        }
        return itemDtoByBookingDtos;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User user = userService.findById(userId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = requestService.findById(itemDto.getRequestId());
        }
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, user, itemRequest)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = findById(itemDto.getId());
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
    public Collection<ItemDto> search(String text, long userId, int from, int size) {
        if (text.isEmpty()) return Collections.emptyList();
        Pageable pageable = Utilities.getPageable(from, size, Sort.unsorted());
        return itemRepository.findItemsByName(text, pageable)
                .stream().map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(CommentDto commentDto, long itemId, long userId) {
        Item item = findById(itemId);
        User user = userService.findById(userId);
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

    public Item findById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + itemId));
    }
}
