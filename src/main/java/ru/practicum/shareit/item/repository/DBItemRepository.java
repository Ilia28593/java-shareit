package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBItemRepository implements ItemRepository {

    private final Map<Long, Item> itemMap;
    private final AtomicInteger ids = new AtomicInteger();

    @Override
    public Optional<Item> create(Item entity) {
        entity.setId(ids.incrementAndGet());
        itemMap.put(entity.getId(), entity);
        return getById(entity.getId());
    }

    @Override
    public Optional<Item> update(Item entity) {
        itemMap.put(entity.getId(), entity);
        return getById(entity.getId());
    }

    @Override
    public void delete(long id) {
        itemMap.remove(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(itemMap.values());
    }

    @Override
    public Optional<Item> getById(long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return itemMap.containsKey(id);
    }

    @Override
    public List<Item> getFromDescription(String text) {
        return Stream.of(
                        itemMap.values().stream()
                                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())),
                        itemMap.values().stream()
                                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .flatMap(Stream::distinct)
                .distinct()
                .filter(item -> item.getAvailable() == true)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllFromUser(long userId) {
        return itemMap.values().stream().filter(u -> u.getOwnerId() == userId).collect(Collectors.toList());
    }
}
