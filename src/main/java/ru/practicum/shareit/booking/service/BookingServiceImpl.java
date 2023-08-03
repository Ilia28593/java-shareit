package ru.practicum.shareit.booking.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.CrudService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl extends CrudService<Booking> implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserServiceImpl userService;

    private final ItemServiceImpl itemService;

    @Override
    public Booking getInfoBooking(long userId, long bookingId) {
        Booking booking = getById(bookingId);
        if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
            return booking;
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException(String.valueOf(userId));
        } else {
            throw new ValidationException(String.valueOf(userId));
        }
    }

    @Override
    public Booking approvedOrRejectedBooking(long userId, long bookingId, boolean approved) {
        Booking booking = getById(bookingId);
        if (booking.getItem().getOwner().getId() != userId)
            throw new NotFoundException(String.valueOf(userId));
        if (booking.getBookingStatus().equals(BookingStatus.APPROVED))
            throw new ValidationException("Status is approved");
        if (approved) {
            booking.setBookingStatus(BookingStatus.APPROVED);
        } else {
            booking.setBookingStatus(BookingStatus.REJECTED);
        }
        return super.update(booking);
    }

    @Override
    public Booking create(long idUser, long idItem, Booking entity) {
        Item item = itemService.getById(idItem);
        validDate(entity, item, idUser);
        entity.setBooker(userService.getById(idUser));
        entity.setItem(item);
        entity.setBookingStatus(BookingStatus.WAITING);
        super.create(entity);
        return bookingRepository.findById(entity.getId()).orElseThrow();
    }

    @Override
    public List<Booking> getListUserBooking(long userId, String status) {
        userService.validateIds(userId);
        switch (status.toUpperCase()) {
            case ("CURRENT"):
                return bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId,
                                LocalDateTime.now(), LocalDateTime.now());

            case ("PAST"):
                return bookingRepository
                        .findAllByBookerIdAndEndBeforeAndBookingStatusOrderByStartDesc(userId,
                                LocalDateTime.now(), BookingStatus.APPROVED);

            case ("FUTURE"):
                return bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now().minusHours(1));

            case ("WAITING"):
                return bookingRepository
                        .findAllByBookerIdAndBookingStatusOrderByStartDesc(userId, BookingStatus.WAITING);

            case ("REJECTED"):
                return bookingRepository.findAllByBookerIdAndBookingStatusOrderByStartDesc(userId, BookingStatus.REJECTED);

            case ("ALL"):
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            default:
                throw new UnsupportedStatusException("Unknown state: " + status);
        }
    }

    @Override
    public List<Booking> getUserListBooking(long userId, String status) {
        userService.validateIds(userId);
        switch (status.toUpperCase()) {
            case ("CURRENT"):
                return bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(userId,
                                LocalDateTime.now(), LocalDateTime.now());

            case ("PAST"):
                return bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeAndBookingStatusOrderByStartDesc(userId,
                                LocalDateTime.now(), BookingStatus.APPROVED);

            case ("FUTURE"):
                return bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now().minusHours(1));

            case ("WAITING"):
                return bookingRepository
                        .findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(userId, BookingStatus.WAITING);

            case ("REJECTED"):
                return bookingRepository.findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(userId, BookingStatus.REJECTED);

            case ("ALL"):
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            default:
                throw new UnsupportedStatusException("Unknown state: " + status);
        }
    }

    private void validDate(@NonNull Booking entity, Item item, long userId) {
        if (entity.getStart().isAfter(entity.getEnd()) || entity.getStart().equals(entity.getEnd())
                || entity.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("incorrect time" + entity);
        }
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ValidationException("incorrect time" + item);
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("this request incorrect for booking item.owner equals user.booking");
        }
    }

    @Override
    protected JpaRepository<Booking, Long> getRepository() {
        return bookingRepository;
    }

    @Override
    protected String getServiceType() {
        return Booking.class.getSimpleName();
    }
}
