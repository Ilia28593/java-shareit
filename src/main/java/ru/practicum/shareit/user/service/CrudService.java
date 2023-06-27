package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.repository.Repository;

import java.util.List;

@Slf4j
public abstract class CrudService<T extends Entity> {
    protected abstract Repository<T> getRepository();

    protected abstract String getServiceType();

    public T create(T entity) {
        log.trace("Request from create entity {}", entity.toString());
        T e = getRepository()
                .create(entity)
                .orElseThrow(() -> new NotFoundException(getServiceType(),
                        Math.toIntExact(entity.getId())));
        log.trace("Object created with id {}.", e.getId());
        return e;
    }

    public T update(T entity) {
        log.trace("Request from update entity {}", entity.toString());
        T e = getRepository()
                .update(entity)
                .orElseThrow(() -> new NotFoundException(getServiceType(), entity.getId()));
        log.trace("Update entity {}", entity.toString());
        return e;
    }

    public void delete(long entity) {
        log.trace("Request from delete entity {}", entity);
        getRepository().delete(entity);
        log.trace("Delete entity {}", entity);
    }

    public List<T> getAll() {
        log.trace("Request from getting all rows from {}", getServiceType());
        List<T> e = getRepository().getAll();
        log.trace("Getting all rows from {}", getServiceType());
        return e;
    }

    public T getById(long id) {
        log.trace("Request from getting entity with type {}, id = {}", getServiceType(), id);
        T e = getRepository()
                .getById(id)
                .orElseThrow(() -> new NotFoundException(getServiceType(), id));
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        return e;
    }


    public void validateIds(long entityId) {
        if (!getRepository().existsById(entityId)) {
            throw new NotFoundException(getServiceType(), entityId);
        }
    }
}
