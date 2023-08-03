package ru.practicum.shareit.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Constants {

    public static final String USER_ID = "X-Sharer-User-Id";

    public static final String VALIDATION_EXCEPTION_ITEM = "Exception from create comment from item id %n";
}
