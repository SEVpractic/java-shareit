package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.impl.ItemController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserController;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemControllerTest {
    private ItemController itemController;
    private UserController userController;

    @Autowired
    public ItemControllerTest(ItemController itemController, UserController userController) {
        this.itemController = itemController;
        this.userController = userController;
    }

    @BeforeEach
    public void prepareUsers() {
        userController.deleteAll();

        UserDto user = UserDto.builder()
                .name("Mortie")
                .email("Notemail@yandex.ru")
                .build();
        userController.create(user);

        UserDto user1 = UserDto.builder()
                .name("Mortie")
                .email("Notemail1@yandex.ru")
                .build();
        userController.create(user1);

        UserDto user2 = UserDto.builder()
                .name("Rick")
                .email("Notemail2@yandex.ru")
                .build();
        userController.create(user2);
    }

    @Test
    public void item_controller_correct() {
        ItemDto item = ItemDto.builder()
                .name("Drilling machine")
                .description("Big and powerful")
                .available(false)
                .build();

        Optional<ItemDto> optionalItem = Optional.of(itemController.create(item, 3L));
        assertThat(optionalItem)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "Drilling machine")
                        .hasFieldOrPropertyWithValue("description", "Big and powerful")
                        .hasFieldOrPropertyWithValue("available", false));

        optionalItem = Optional.of(itemController.getById(1));
        assertThat(optionalItem)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "Drilling machine")
                        .hasFieldOrPropertyWithValue("description", "Big and powerful")
                        .hasFieldOrPropertyWithValue("available", false));

        Optional<List<ItemDto>> optionalItems = Optional.of(itemController.getAllByUserId(3L));
        assertThat(optionalItems)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasSize(1));

        optionalItems = Optional.of(itemController.getAllByText("machine"));
        assertThat(optionalItems)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .isEmpty());

        item = item.toBuilder().available(true).build();
        optionalItem = Optional.of(itemController.update(1L, 3L, item));
        assertThat(optionalItem)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("name", "Drilling machine")
                        .hasFieldOrPropertyWithValue("description", "Big and powerful")
                        .hasFieldOrPropertyWithValue("available", true));

        optionalItems = Optional.of(itemController.getAllByText("Big"));
        assertThat(optionalItems)
                .isPresent()
                .hasValueSatisfying(o ->assertThat(o)
                        .hasSize(1));

        itemController.deleteById(1L);
        optionalItems = Optional.of(itemController.getAllByText("Big"));
        assertThat(optionalItems)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .isEmpty());

        itemController.create(item, 3L);
        itemController.deleteAll();
        optionalItems = Optional.of(itemController.getAllByText("Big"));
        assertThat(optionalItems)
                .isPresent()
                .hasValueSatisfying(o -> assertThat(o)
                        .isEmpty());
    }
}
