package ru.practicum.shareit.config;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DataUtilsService {

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }


}