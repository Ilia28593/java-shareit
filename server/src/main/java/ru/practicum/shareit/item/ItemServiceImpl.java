package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.Utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoWithBookingDto getById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemId)));
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(String.format("item request %s not found", itemDto.getRequestId())));
        }
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, user, itemRequest)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long userId) {
        Item item = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemDto.getId())));
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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("item %s not found", itemId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("user %s not found", userId)));
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
}
