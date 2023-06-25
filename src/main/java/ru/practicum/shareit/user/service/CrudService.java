package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.repository.Repository;

import java.util.List;

@Slf4j
public abstract class CrudService<T extends Entity> extends Service<T> {
    protected abstract Repository<T> getRepository();

    protected Repository<T> getReadRepository() {
        return this.getRepository();
    }

    public T create(T entity) {
        log.trace("Create entity {}", entity.toString());
        return this.getRepository()
                .create(entity)
                .orElseThrow(() -> getNoDataFoundException(Math.toIntExact(entity.getId())));
    }

    public T update(T entity) {
        log.trace("Update entity {}", entity.toString());
        return this.getRepository()
                .update(entity)
                .orElseThrow(() -> getNoDataFoundException(Math.toIntExact(entity.getId())));
    }

    public void delete(long entity) {
        log.trace("Delete entity {}", entity);
        this.getRepository().delete(entity);
    }

    public List<T> getAll() {
        log.trace("Getting all rows from {}", getServiceType());
        return this.getRepository().getAll();
    }

    public T getById(long id) {
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        return this.getRepository()
                .getById(id)
                .orElseThrow(() -> getNoDataFoundException(id));
    }

    public void validateIds(long... entityIds) {
        for (long entityId : entityIds) {
            if (!this.getRepository().existsById(entityId)) {
                throw getNoDataFoundException(entityId);
            }
        }
    }
}
