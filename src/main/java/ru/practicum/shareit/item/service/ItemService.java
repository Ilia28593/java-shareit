package ru.practicum.shareit.item.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.Repository;
import ru.practicum.shareit.user.service.CrudService;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemService extends CrudService<Item> {
    private final ItemRepository itemRepository;

    private final UserService userService;

    @Override
    public Item getById(long id) {
        return super.getById(id);
    }

    public Item create(long idUser, Item entity) {
        userService.validateIds(idUser);
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new ValidationException(String.valueOf(idUser));
        }
        entity.setOwnerId(idUser);
        return super.create(entity);
    }

    public Item update(long userId, long itemId, Item entity) {
        userService.validateIds(userId);
        validateIds(itemId);
        if (getById(itemId).getOwnerId() == userId) {
            Item item = getById(itemId);
            item.setName(entity.getName() != null ? entity.getName() : item.getName());
            item.setRequest(entity.getRequest() != null ? entity.getRequest() : item.getRequest());
            item.setDescription(entity.getDescription() != null ? entity.getDescription() : item.getDescription());
            item.setAvailable(entity.getAvailable() != null ? entity.getAvailable() : item.getAvailable());
            return super.update(item);
        }
        throw new NotFoundException(String.valueOf(userId));
    }

    public List<Item> getFromDescription(long userId, @NonNull String text) {
        userService.validateIds(userId);
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.getFromDescription(text);
    }

    public List<Item> getAll(long userId) {
        return itemRepository.getAllFromUser(userId);
    }

    @Override
    protected String getServiceType() {
        return Item.class.getSimpleName();
    }

    @Override
    protected Repository<Item> getRepository() {
        return itemRepository;
    }
}
