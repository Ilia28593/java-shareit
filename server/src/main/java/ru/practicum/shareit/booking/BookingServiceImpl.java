package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStateFetchByBooker.BookingStateFetchBookerStrategyFactory;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.config.Utilities;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.PermissionViolationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.config.Constants.BOOKING_NO_FOUND_BY_ID;
import static ru.practicum.shareit.config.Constants.ITEM_NO_FOUND_FROM_ID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final BookingStateFetchBookerStrategyFactory bookingStateFetchBookerStrategyFactory;

    @Override
    public BookingResponseDto create(BookingRequestDto bookingRequestDto, long userId) {
        User user = getUser(userId);
        Item item = itemService.getItemById(bookingRequestDto.getItemId());
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("item %s not available", item.getId()));
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException(String.format("user can not book its own item", item.getId()));
        }
        Booking booking = bookingMapper.toBooking(bookingRequestDto, item, user);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approve(long bookingId, boolean approved, long userId) {
        Booking booking = getBooking(bookingId,userId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new PermissionViolationException("Only item owner can approve booking");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException(String.format("booking %s is already approved", bookingId));
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getById(long bookingId, long userId) {
        User user = getUser(userId);
        Booking booking = getBooking(bookingId,userId);
        if (!Objects.equals(booking.getBooker(), user) && !Objects.equals(booking.getItem().getOwner(), user)) {
            throw new NotFoundException("Booking was not booked by user and item owner is different");
        }
        return bookingRepository.findById(bookingId)
                .map(bookingMapper::toBookingDtoResponse)
                .orElseThrow(() -> new NotFoundException(String.format("booking %s not found", bookingId)));
    }

    @Override
    public Collection<BookingResponseDto> getAllByBookerId(BookingStatusFilter state, long userId, int from, int size) {
        User user = getUser(userId);
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("start").descending());
        Collection<Booking> bookings = bookingStateFetchBookerStrategyFactory.findStrategy(state).fetch(user, pageable);
        return bookings.stream().map(bookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDto> getAllByItemOwnerId(BookingStatusFilter state, long userId, int from, int size) {
        User user = getUser(userId);
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

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ITEM_NO_FOUND_FROM_ID + userId));
    }

    private Booking getBooking(long bookingId, long userId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(BOOKING_NO_FOUND_BY_ID + userId));
    }
}
