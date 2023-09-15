package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto, Item item, User user);

    CommentResponseDto toCommentResponseDto(Comment comment);

    Collection<CommentResponseDto> toCommentsResponseDto(Collection<Comment> comments);
}
