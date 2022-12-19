package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserController;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserControllerTest {
    private UserController userController;

    @Autowired
    public UserControllerTest(UserController userController) {
        this.userController = userController;
    }

    @Test
    public void user_controller_correct() {
        UserDto user = UserDto.builder()
                .name("Mortie")
                .email("Email@yandex.ru")
                .build();

        Optional<UserDto> optionalUser = Optional.of(userController.create(user));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 4L)
                        .hasFieldOrPropertyWithValue("name", "Mortie")
                        .hasFieldOrPropertyWithValue("email", "Email@yandex.ru"));

        user = UserDto.builder().name("Rick").build();
        optionalUser = Optional.of(userController.update(user, 4L));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 4L)
                        .hasFieldOrPropertyWithValue("name", "Rick")
                        .hasFieldOrPropertyWithValue("email", "Email@yandex.ru"));

        user = UserDto.builder().email("Email@gmail.com").build();
        optionalUser = Optional.of(userController.update(user, 4L));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 4L)
                        .hasFieldOrPropertyWithValue("name", "Rick")
                        .hasFieldOrPropertyWithValue("email", "Email@gmail.com"));

        Optional<List<UserDto>> optionalUsers = Optional.of(userController.getAll());
        assertThat(optionalUsers)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasSize(4));

        optionalUser = Optional.of(userController.getById(4L));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 4L)
                        .hasFieldOrPropertyWithValue("name", "Rick")
                        .hasFieldOrPropertyWithValue("email", "Email@gmail.com"));

        userController.deleteById(4L);
        optionalUsers = Optional.of(userController.getAll());
        assertThat(optionalUsers)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasSize(3));

        userController.create(user);
        userController.deleteAll();
        optionalUsers = Optional.of(userController.getAll());
        assertThat(optionalUsers)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .isEmpty());
    }
}
