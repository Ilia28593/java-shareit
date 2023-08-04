package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;




@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available")
    @Builder.Default
    private Boolean available = false;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @ToString.Exclude
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionTable(name = "requests", joinColumns = @JoinColumn(name = "id"))
    private ItemRequest request;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }
}
