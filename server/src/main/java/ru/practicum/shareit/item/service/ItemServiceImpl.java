package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInItemResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.config.Utilities;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.ITEM_NO_FOUND_FROM_ID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoWithBookingDto getById(long itemId, long userId) {
        Item item = getItemById(itemId);
        BookingInItemResponseDto lastBooking = null;
        BookingInItemResponseDto nextBooking = null;
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
    public Collection<ItemDtoWithBookingDto> findByUserId(long userId, int from, int size) {
        Collection<ItemDtoWithBookingDto> itemDtoWithBookingDtos = new ArrayList<>();
        Collection<Item> items;
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("id").ascending());
        items = itemRepository.findItemsByOwnerId(userId, pageable);
        for (Item item : items) {
            BookingInItemResponseDto lastBooking = bookingRepository.findLastBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            BookingInItemResponseDto nextBooking = bookingRepository.findNextBooking(item.getId())
                    .map(bookingMapper::toBookingInItemDtoResponse)
                    .orElse(null);
            Collection<CommentResponseDto> commentsResponseDto = commentMapper
                    .toCommentsResponseDto(commentRepository.findCommentsByItem(item));
            itemDtoWithBookingDtos.add(itemMapper.toItemDtoWithBookingDto(item, lastBooking, nextBooking, commentsResponseDto));
        }
        return itemDtoWithBookingDtos;
    }

    @Transactional
    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User user = userService.getUserById(userId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestService.getItemRequestById(itemDto.getRequestId());
        }
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, user, itemRequest)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = getItemById(itemDto.getId());
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
        Item item = getItemById(itemId);
        User user = userService.getUserById(userId);
        Collection<Booking> completedBookings = bookingRepository
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(user, item, LocalDateTime.now(), BookingStatus.APPROVED);
        if (!completedBookings.isEmpty()) {
            Comment comment = commentMapper.toComment(commentDto, item, user);
            commentRepository.save(comment);
            return commentMapper.toCommentResponseDto(comment);
        } else {
            throw new BadRequestException("User has no completed booking for item");
        }
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + itemId));
    }
}
