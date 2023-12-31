package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findAllByRequester(User requester);

    List<ItemRequest> findAllByRequesterIsNot(User requester, Pageable pageable);
}
