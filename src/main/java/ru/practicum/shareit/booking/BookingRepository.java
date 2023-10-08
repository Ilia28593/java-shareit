package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBooker(User booker, Pageable pageable);

    List<Booking> findBookingsByBookerAndStatus(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findBookingsByBookerAndStatusInAndEndBefore(User booker, Collection<BookingStatus> status, LocalDateTime end, Pageable pageable);

    List<Booking> findBookingsByBookerAndStatusInAndStartAfter(User booker, Collection<BookingStatus> status, LocalDateTime start, Pageable pageable);

    List<Booking> findBookingsByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findBookingsByItem_Owner(User owner, Pageable pageable);

    List<Booking> findBookingsByItem_OwnerAndStatus(User owner, BookingStatus status, Pageable pageable);

    List<Booking> findBookingsByItem_OwnerAndStatusInAndEndBefore(User owner, Collection<BookingStatus> status, LocalDateTime end, Pageable pageable);

    List<Booking> findBookingsByItem_OwnerAndStatusInAndStartAfter(User owner, Collection<BookingStatus> status, LocalDateTime start, Pageable pageable);

    List<Booking> findBookingsByItem_OwnerAndStartBeforeAndEndAfter(User owner, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking as b " +
            "where b.item.id = :itemId " +
            "and (b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED or " +
            "b.status = ru.practicum.shareit.booking.BookingStatus.WAITING) " +
            "and b.start = " +
            "(select max(bMax.start)  from Booking as bMax " +
            "where bMax.item.id = :itemId " +
            "and bMax.start <= current_timestamp )")
    Optional<Booking> findLastBooking(long itemId);

    @Query("select b from Booking as b " +
            "where b.item.id = :itemId " +
            "and (b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED or " +
            "b.status = ru.practicum.shareit.booking.BookingStatus.WAITING) " +
            "and b.start = " +
            "(select min(bMin.start)  from Booking as bMin " +
            "where bMin.item.id = :itemId " +
            "and bMin.start >= current_timestamp )")
    Optional<Booking> findNextBooking(long itemId);

    Collection<Booking> findBookingsByBookerAndItemAndEndBeforeAndStatus(User booker, Item item, LocalDateTime end, BookingStatus status);
}
