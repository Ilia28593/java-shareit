package ru.practicum.shareit.item.reposotory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findItemsByOwnerId(long ownerId);

    @Query("select i from Item as i" +
            " where (lower(i.description) like lower(concat('%', :text, '%')) " +
            " or lower(i.name) like lower(concat('%', :text, '%')))" +
            " and i.available = true")
    Collection<Item> findItemsByName(String text);
}
