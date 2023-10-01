package ru.practicum.shareit.config;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DataUtils {
    private LocalDateTime currentLocalTime = LocalDateTime.now();

}
