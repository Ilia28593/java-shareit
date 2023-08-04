package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Getter
@Setter
@ToString
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "id"))
    private Item item;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus bookingStatus;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }
}
