package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface CommentMapper {
    Comment toComment(CommentDto commentDto, Item item, User user);

    CommentResponseDto toCommentResponseDto(Comment comment);

    Collection<CommentResponseDto> toCommentsResponseDto(Collection<Comment> comments);
}
