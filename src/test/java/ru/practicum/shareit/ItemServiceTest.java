package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserIncomeDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;

    /*@Test
    @Order(0)
    void fillUserService() {
        UserIncomeDto userCreateDto = UserIncomeDto.builder()
                .name("user")
                .email("user@yandex.ru")
                .build();
        userService.create(userCreateDto);

        userCreateDto = UserIncomeDto.builder()
                .name("user1")
                .email("user1@yandex.ru")
                .build();
        userService.create(userCreateDto);

        List<UserDto> users = userService.getAll();

        assertThat(users)
                .hasSize(2)
                .map(UserDto::getId)
                .contains(1L, 2L);
    }*/

    @Test
    @Order(1)
    void createTest() {
        UserIncomeDto userCreateDto = UserIncomeDto.builder()
                .name("user")
                .email("user@yandex.ru")
                .build();
        userService.create(userCreateDto);

        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("users 1 item")
                .available(false)
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.create(incomeDto, 1L));

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", false);
                });
    }
}
