package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DBUserRepository implements UserRepository {
    private final Map<Long, User> userMap;
    private final AtomicInteger ids = new AtomicInteger();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return userMap.containsKey(id);
    }

    @Override
    public Optional<User> create(User entity) {
        entity.setId(ids.incrementAndGet());
        userMap.put(entity.getId(), entity);
        return getById(entity.getId());
    }

    @Override
    public Optional<User> update(User entity) {
        userMap.put(entity.getId(), entity);
        return getById(entity.getId());
    }

    @Override
    public void delete(long id) {
        userMap.remove(id);
    }


    public boolean existEmail(String email) {
        return userMap.values().stream().map(User::getEmail).anyMatch(e -> e.equals(email));
    }
}
