package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistsException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.repository.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService extends CrudService<User> {
    private final UserRepository userRepository;

    @Override
    protected Repository<User> getRepository() {
        return this.userRepository;
    }

    @Override
    public User create(User entity) {
        if (entity.getEmail() == null || entity.getEmail().isBlank()) {
            throw new ValidationException("User email is null or blank");
        } else if (entity.getName() == null || entity.getName().isBlank()) {
            throw new ValidationException("User name is null or blank");
        } else {
            checkValidEmail(entity.getEmail());
            return super.create(entity);
        }
    }

    public User update(long id, User entity) {
        User u = getById(id);
        entity.setId(id);
        if(entity.getEmail() != null) {
            if (!u.getEmail().equals(entity.getEmail()))checkValidEmail(entity.getEmail());
        }
        u.setEmail(entity.getEmail() != null ? entity.getEmail() : u.getEmail());
        u.setName(entity.getName() != null ? entity.getName() : u.getName());
        return super.update(u);
    }

    private void checkValidEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValidationException(String.format("User email invalid %s", email));
        } else if (userRepository.existEmail(email)) {
            throw new ObjectAlreadyExistsException(String.format("User email %s is exist", email));
        }
    }

    @Override
    protected String getServiceType() {
        return User.class.getSimpleName();
    }
}
