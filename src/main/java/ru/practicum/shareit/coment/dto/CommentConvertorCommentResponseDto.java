package ru.practicum.shareit.coment.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.coment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConvertorCommentResponseDto implements Converter<Comment, CommentResponseDto> {

    @Override
    public CommentResponseDto convert(Comment source) {
        return CommentResponseDto.builder()
                .id(source.getId())
                .authorName(source.getAuthor().getName())
                .text(source.getText())
                .created(source.getCreated())
                .build();
    }

    public List<CommentResponseDto> getListResponse(List<Comment> list) {
        return list.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
