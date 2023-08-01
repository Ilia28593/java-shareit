package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.NotFoundException;

public abstract class Service<T extends Entity> {
    private final String notFoundExceptionMessage = "%s with id %s not found";

    protected abstract String getServiceType();

    NotFoundException getNoDataFoundException(long id) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, getServiceType(), id));
    }

    NotFoundException getNoDataFoundException(long id, String serviceType) {
        return new NotFoundException(String.format(this.notFoundExceptionMessage, serviceType, id));
    }
}
