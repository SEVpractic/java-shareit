package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

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
        User user = userStorage.getById(id);
        log.info("Возвращен пользователь c id = {} ", id);
        return user;
    }

    @Override
    public User create(User user) {
        user = userStorage.create(user);
        log.info("Создан пользователь c id = {} ", user.getId());
        return user;
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
