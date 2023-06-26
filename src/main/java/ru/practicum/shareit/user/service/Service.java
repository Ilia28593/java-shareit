package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.NotFoundException;

public abstract class Service {
    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "%s with id %s not found";

    protected abstract String getServiceType();

    NotFoundException getNoDataFoundException(long id) {
        return new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, getServiceType(), id));
    }

    NotFoundException getNoDataFoundException(long id, String serviceType) {
        return new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, serviceType, id));
    }
}
