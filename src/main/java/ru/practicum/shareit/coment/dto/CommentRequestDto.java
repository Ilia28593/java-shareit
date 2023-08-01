package ru.practicum.shareit.coment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentRequestDto {
    private String text;
}
