package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationExceptionUpdateItem extends RuntimeException {

    public ValidationExceptionUpdateItem(long message) {

        super(String.format("Inappropriate user %l", message));
    }
}
