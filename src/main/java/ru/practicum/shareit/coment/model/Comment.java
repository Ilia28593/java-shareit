package ru.practicum.shareit.coment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @ToString.Exclude
    private User author;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "id"))
    private Item item;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime created;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }
}
