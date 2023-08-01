package ru.practicum.shareit.coment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class CommentResponseDto {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
