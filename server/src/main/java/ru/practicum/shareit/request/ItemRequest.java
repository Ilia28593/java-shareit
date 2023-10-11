package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "item_requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User requester;
    private LocalDateTime created;
    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "request_id")
    private Collection<Item> items;
}
