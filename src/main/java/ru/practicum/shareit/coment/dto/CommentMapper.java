package ru.practicum.shareit.coment.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.coment.model.Comment;

@Mapper
public interface CommentMapper {
    Comment mapToComment(CommentRequestDto dto);
}
