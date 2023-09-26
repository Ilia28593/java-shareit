package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(commentDto.getText());
        return commentDto;
    }

    @Override
    public Comment toComment(CommentDto commentDto, Item item, User user) {
        return new Comment()
                .setText(commentDto.getText())
                .setItem(item)
                .setAuthor(user)
                .setCreated(LocalDateTime.now());
    }

    @Override
    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto()
                .setId(comment.getId())
                .setText(comment.getText())
                .setAuthorName(comment.getAuthor().getName())
                .setCreated(comment.getCreated());
    }

    @Override
    public Collection<CommentResponseDto> toCommentsResponseDto(Collection<Comment> comments) {
        return comments.stream().map(this::toCommentResponseDto).collect(Collectors.toList());
    }
}
