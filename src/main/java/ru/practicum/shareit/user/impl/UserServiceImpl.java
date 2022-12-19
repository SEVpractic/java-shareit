package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAll() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAll();
    }

    @Override
    public User getById(long id) {
        log.info("Возвращен пользователь c id = {} ", id);
        return userStorage.getById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Пользователь c id = %s не существует", id))
                );
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        user = userStorage.update(user);
        log.info("Обновлен пользователь c id = {} ", user.getId());
        return user;
    }

    @Override
    public void deleteById(long id) {
        userStorage.deleteById(id);
        log.info("Удален пользователь c id = {} ", id);
    }

    @Override
    public void deleteAll() {
        userStorage.deleteAll();
        log.info("Удалены все пользователи");
    }
}
