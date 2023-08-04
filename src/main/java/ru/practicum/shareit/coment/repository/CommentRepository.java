package ru.practicum.shareit.coment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.coment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemIdOrderByCreatedDesc(long itemId);
}
