package ru.practicum.shareit.user.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T>  {
    Optional<T> create(T entity);

    Optional<T> update(T entity);

    void delete(long id);

    List<T> getAll();

    Optional<T> getById(long id);

    boolean existsById(long id);
}
