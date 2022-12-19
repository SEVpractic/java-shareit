package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    Optional<User> getById(long id);

    User create(User user);

    User update(User user);

    void deleteById(long id);

    void deleteAll();
}
