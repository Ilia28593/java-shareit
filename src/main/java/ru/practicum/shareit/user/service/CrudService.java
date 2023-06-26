package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.repository.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class CrudService<T extends Entity> extends Service {
    protected abstract Repository<T> getRepository();

    protected Repository<T> getReadRepository() {
        return this.getRepository();
    }

    public T create(T entity) {
        log.trace("Create entity {}", entity.toString());
        T e = getRepository()
                .create(entity)
                .orElseThrow(() -> getNoDataFoundException(Math.toIntExact(entity.getId())));
        log.trace("Object created with id {}.", e.getId());
        return e;
    }

    public T update(T entity) {
        log.trace("Update entity {}", entity.toString());
        return this.getRepository()
                .update(entity)
                .orElseThrow(() -> getNoDataFoundException(Math.toIntExact(entity.getId())));
    }

    public void delete(long entity) {
        log.trace("Request delete entity {}", entity);
        this.getRepository().delete(entity);
        log.trace("Delete entity {}", entity);
    }

    public List<T> getAll() {
        log.trace("Getting all rows from {}", getServiceType());
        return this.getRepository().getAll();
    }

    public T getById(long id) {
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        validateIds(id);
        return this.getRepository()
                .getById(id)
                .orElseThrow(() -> new NotFoundException(getServiceType(), id));
    }


    public void validateIds(long entityId) {
        if (!this.getRepository().existsById(entityId)) {
            throw new NotFoundException(getServiceType(),entityId);
        }
    }
}
