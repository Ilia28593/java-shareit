package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends Repository<Item> {
    Optional<Item> create(Item entity);

    List<Item> getFromDescription(String text);

    List<Item> getAllFromUser(long userId);
}
