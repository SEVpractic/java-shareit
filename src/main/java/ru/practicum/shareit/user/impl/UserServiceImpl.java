package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        log.info("Возвращен список всех пользователей");
        return userRepository.findAll();
    }

    @Override
    public User getById(long id) {
        log.info("Возвращен пользователь c id = {} ", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Пользователь c id = %s не существует", id))
                );
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        User oldUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Пользователь c id = %s не существует", user.getId()))
                );

        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            oldUser.setEmail(user.getEmail());
        }

        oldUser = userRepository.save(oldUser);

        log.info("Обновлен пользователь c id = {} ", user.getId());
        return oldUser;
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь c id = {} ", id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
        log.info("Удалены все пользователи");
    }
}
