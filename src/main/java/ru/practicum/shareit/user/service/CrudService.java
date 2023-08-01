package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

@Slf4j
public abstract class CrudService<T> {
    protected abstract JpaRepository<T, Long> getRepository();

    protected abstract String getServiceType();

    public T create(T entity) {
        log.trace("Request from create entity {}", entity.toString());
        T e = getRepository()
                .save(entity);
        log.trace("Object created.");
        return e;
    }

    public T update(T entity) {
        log.trace("Request from update entity {}", entity.toString());
        T e = getRepository()
                .save(entity);
        log.trace("Update entity {}", entity);
        return e;
    }

    public void delete(long entity) {
        log.trace("Request from delete entity {}", entity);
        getRepository().delete(getById(entity));
        log.trace("Delete entity {}", entity);
    }

    public List<T> getAll() {
        log.trace("Request from getting all rows from {}", getServiceType());
        List<T> e = getRepository().findAll();
        log.trace("Getting all rows from {}", getServiceType());
        return e;
    }

    public T getById(long id) {
        log.trace("Request from getting entity with type {}, id = {}", getServiceType(), id);
        validateIds(id);
        T e = getRepository()
                .getById(id);
        log.trace("Getting entity with type {}, id = {}", getServiceType(), id);
        return e;
    }


    public void validateIds(long entityId) {
        if (!getRepository().existsById(entityId)) {
            throw new NotFoundException(String.valueOf(entityId));
        }
    }
}
