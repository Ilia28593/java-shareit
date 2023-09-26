package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingStatusFilter;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.config.Utilities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingStateFetchBookerStrategyFactory bookingStateFetchBookerStrategyFactory;

    @Override
    public BookingResponseDTO create(BookingRequestDto bookingRequestDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + bookingRequestDto.getItemId()));
        if (!item.getAvailable()) {
            throw new BadRequestException(ITEM_NOT_AVAILABLE_BY_ID + item.getId());
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException(String.format("user can not book its own item", item.getId()));
        }
        Booking booking = bookingMapper.toBooking(bookingRequestDto, item, user);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO approved(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NO_FOUND_BY_ID + bookingId));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new PermissionViolationException("Only item owner can approve booking");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException(BOOKING_NOT_AVAILABLE_BY_ID + bookingId);
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO getById(long bookingId, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NO_FOUND_BY_ID + bookingId));
        if (!Objects.equals(booking.getBooker(), user) && !Objects.equals(booking.getItem().getOwner(), user)) {
            throw new NotFoundException("Booking was not booked by user and item owner is different");
        }
        return bookingRepository.findById(bookingId)
                .map(bookingMapper::toBookingDtoResponse)
                .orElseThrow(() -> new NotFoundException(BOOKING_NO_FOUND_BY_ID + bookingId));
    }

    @Override
    public Collection<BookingResponseDTO> getAllByBookerId(BookingStatusFilter state, long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("start").descending());
        Collection<Booking> bookings = bookingStateFetchBookerStrategyFactory.findStrategy(state).fetch(user, pageable);
        return bookings.stream().map(bookingMapper::toBookingDtoResponse).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDTO> getAllByItemOwnerId(BookingStatusFilter state, long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new NotFoundException(USER_NO_FOUND_FROM_ID + userId));
        Collection<Booking> bookings;
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("start").descending());
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStartBeforeAndEndAfter(
                        user, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatus(user, BookingStatus.REJECTED, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatus(user, BookingStatus.WAITING, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusInAndEndBefore(
                        user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusInAndStartAfter(
                        user, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now(), pageable);
                break;
            default:
                bookings = bookingRepository.findBookingsByItem_Owner(user, pageable);
                break;
        }
        return bookings.stream().map(bookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }
}
