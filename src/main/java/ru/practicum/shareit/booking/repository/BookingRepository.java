package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.id = ?1 ")
    Optional<Booking> findById(Long id);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item " +
            "WHERE b.item.id = ?1 " +
            "and b.bookingStatus = 'APPROVED' " +
            "order by b.end desc")
    List<Booking> findLastBookingByIdItemId(Long id);

    List<Booking> findAllByItemIdAndBookerIdAndBookingStatusAndEndBefore(long itemId, long userId, BookingStatus bookingStatus,
                                                                         LocalDateTime localDateTime);
    List<Booking> findAllByBookerIdAndEndBeforeAndBookingStatusOrderByStartDesc(Long bookerId, LocalDateTime current,
                                                                                BookingStatus status);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(Long bookerId, LocalDateTime current,
                                                                         LocalDateTime currents);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime current);

    List<Booking> findAllByBookerIdAndBookingStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeAndBookingStatusOrderByStartDesc(Long ownerId, LocalDateTime end,
                                                                                   BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(Long ownerId,
                                                                            LocalDateTime current,
                                                                            LocalDateTime currents);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime current);

    List<Booking> findAllByItemOwnerIdAndBookingStatusOrderByStartDesc(Long ownerId, BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

}
