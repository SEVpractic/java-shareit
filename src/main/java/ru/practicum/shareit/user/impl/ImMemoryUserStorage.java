package ru.practicum.shareit.user.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.CreationErrorException;
import ru.practicum.shareit.util.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.*;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ImMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        emailDuplicatesCheck(user);
        updateId(user);

        if (users.containsKey(user.getId())) {
            log.info("Не удалось создать ползователя с id = {}", user.getId());
            throw new CreationErrorException(String.format("Не удалось создать ползователя с id = %s", user.getId()));
        }

        users.putIfAbsent(user.getId(), user);
        log.info("Создан пользователь c id = {} ", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        emailDuplicatesCheck(user);
        User oldUser = users.get(user.getId());

        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }

        return oldUser;
    }

    @Override
    public void deleteById(long id) {
        if (users.remove(id) == null) {
            log.info("Пользователь c id = {} не существует", id);
            throw new EntityNotExistException(
                    String.format("Пользователь c id = %s не существует", id)
            );
        }
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    private void updateId(User user) {
        id++;
        user.setId(id);
    }

    private void emailDuplicatesCheck(User user) {
        String email = user.getEmail();

        if (users.containsKey(user.getId()) && users.get(user.getId()).getEmail().equals(user.getEmail())) {
            return;
        }

        users.values().forEach(u -> {
            if (u.getEmail().equals(email)) {
                log.info("Пользователь c email = {} уже существует", email);
                throw new EmailAlreadyExistException(
                        String.format("Пользователь c email = %s уже существует", email)
                );
            }
        });
    }
}
