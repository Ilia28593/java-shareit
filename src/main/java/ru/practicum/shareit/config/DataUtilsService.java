package ru.practicum.shareit.config;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Service
public class DataUtilsService {

    public LocalDateTime getCurrentLocalTime() {
        return LocalDateTime.now();
    }


}
