package ru.practicum.shareit.item.dto;


import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class CommentDto {
    private String text;
}
