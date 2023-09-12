package ru.practicum.shareit.booking.reposotory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findBookingsByBookerOrderByStartDesc(User booker);

    Collection<Booking> findBookingsByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    Collection<Booking> findBookingsByBookerAndStatusInAndEndBeforeOrderByStartDesc(User booker, Collection<BookingStatus> status, LocalDateTime end);

    Collection<Booking> findBookingsByBookerAndStatusInAndStartAfterOrderByStartDesc(User booker, Collection<BookingStatus> status, LocalDateTime start);

    Collection<Booking> findBookingsByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findBookingsByItem_OwnerOrderByStartDesc(User owner);

    Collection<Booking> findBookingsByItem_OwnerAndStatusOrderByStartDesc(User owner, BookingStatus status);

    Collection<Booking> findBookingsByItem_OwnerAndStatusInAndEndBeforeOrderByStartDesc(User owner, Collection<BookingStatus> status, LocalDateTime end);

    Collection<Booking> findBookingsByItem_OwnerAndStatusInAndStartAfterOrderByStartDesc(User owner, Collection<BookingStatus> status, LocalDateTime start);

    Collection<Booking> findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking as b " +
            "where b.item.id = :itemId " +
            "and (b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED or " +
            "b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING) " +
            "and b.start = " +
            "(select max(bMax.start)  from Booking as bMax " +
            "where bMax.item.id = :itemId " +
            "and bMax.start <= current_timestamp )")
    Optional<Booking> findLastBooking(long itemId);

    @Query("select b from Booking as b " +
            "where b.item.id = :itemId " +
            "and (b.status = ru.practicum.shareit.booking.model.BookingStatus.APPROVED or " +
            "b.status = ru.practicum.shareit.booking.model.BookingStatus.WAITING) " +
            "and b.start = " +
            "(select min(bMin.start)  from Booking as bMin " +
            "where bMin.item.id = :itemId " +
            "and bMin.start >= current_timestamp )")
    Optional<Booking> findNextBooking(long itemId);

    Collection<Booking> findBookingsByBookerAndItemAndEndBeforeAndStatus(User booker, Item item, LocalDateTime end, BookingStatus status);
}
