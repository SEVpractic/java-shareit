package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIncomeDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        log.info("Возвращен список всех пользователей");
        return UserMapper.toUserDto(users);
    }

    @Override
    public UserDto getById(long userId) {
        User user = findById(userId);
        log.info("Возвращен пользователь c id = {} ", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto create(UserIncomeDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        log.info("Создан пользователь c id = {} ", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserIncomeDto userDto, long userId) {
        User user = findById(userId);

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }

        log.info("Обновлен пользователь c id = {} ", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь c id = {} ", id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
        log.info("Удалены все пользователи");
    }

    private User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Пользователь c id = %s не существует", userId))
                );
    }
}
