package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final ItemService itemService;

    @Test
    @Order(0)
    @Sql(value = {"/test-schema.sql", "/users-create-test.sql" })
    void createTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("users 1 item")
                .available(false)
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.create(incomeDto, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", false);
                });
    }

    @Test
    @Order(1)
    void updateAvailableTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .available(true)
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(2)
    void updateDescriptionTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .description("users 1 updated item")
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(3)
    void updateNameTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("updated item")
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(4)
    void updateUserIdUnCorrectTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("updated item")
                .description("users 1 updated item")
                .available(true)
                .build();

        UpdateErrorException exception = assertThrows(UpdateErrorException.class,
                () -> itemService.update(incomeDto, 1L, 2L));

        Assertions.assertThat(exception)
                .hasMessage("Ошибка обновления вещи c id = 1 , невозможно обновить вещь другого пользователя");
    }

    @Test
    @Order(5)
    void updateItemIdUnCorrectTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("updated item")
                .description("users 1 updated item")
                .available(true)
                .build();

        assertThrows(EntityNotExistException.class, () -> itemService.update(incomeDto,10L, 1L));
    }

    @Test
    @Order(6)
    @Sql(value = {"/bookings-create-test.sql"})
    void addCommentTest() {
        CommentDto incomeCommentDto = CommentDto.builder()
                .text("text 1 comment")
                .authorName("comment 1 authorName")
                .build();
        Optional<CommentDto> commentDto = Optional.of(itemService.addComment(incomeCommentDto, 1L, 2L));

        Assertions.assertThat(commentDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("text", "text 1 comment");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("authorName", "name1");
                });
    }

    @Test
    @Order(7)
    void getByWrongIdTest() {
        assertThrows(EntityNotExistException.class, () -> itemService.getById(100L, 1L));
    }

    @Test
    @Order(8)
    void getByIdForOwnerTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getById(1L, 1L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrProperty("lastBooking");
                    Assertions.assertThat(i.getLastBooking()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    Assertions.assertThat(i).hasFieldOrProperty("nextBooking");
                    Assertions.assertThat(i.getNextBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrProperty("comments");
                    Assertions.assertThat(i.getComments()).hasSize(1);
                });
    }

    @Test
    @Order(9)
    void getByIdNotForOwnerTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getById(1L, 3L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i.getLastBooking()).isNull();
                    Assertions.assertThat(i).hasFieldOrProperty("comments");
                    Assertions.assertThat(i.getComments()).hasSize(1);
                });
    }

    @Test
    @Order(10)
    void getAllByOwnerIdTest() {
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);
        Assertions.assertThat(items)
                .hasSize(1);

        items = itemService.getAllByUserId(0, 10, 3L);
        Assertions.assertThat(items)
                .isEmpty();
    }

    @Test
    @Order(11)
    void getAllByTextTest() {
        List<ItemDto> items = itemService.getAllByText(0, 10, "item");

        Assertions.assertThat(items)
                .hasSize(1);

        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(12)
    @Sql(value = {"/item-create-test.sql"})
    void getAllByUserIdPaginationTest() {
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);

        Assertions.assertThat(items)
                .hasSize(6);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "updated item");

        items = itemService.getAllByUserId(3, 2, 1L);

        Assertions.assertThat(items)
                .hasSize(2);
        Assertions.assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item2");
    }

    @Test
    @Order(13)
    void deleteAll() {
        itemService.deleteAll();
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);

        Assertions.assertThat(items)
                .isEmpty();
    }

    @Test
    @Order(14)
    @Sql(value = {"/request-create-test.sql"})
    void createTestWithRequest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("users 3 item")
                .available(true)
                .requestId(1L)
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.create(incomeDto, 3L));

        Assertions.assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("id", 7L);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("description", "users 3 item");
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    Assertions.assertThat(i).hasFieldOrPropertyWithValue("requestId", 1L);
                });
    }

    @Test
    @Order(15)
    void createRequestNotExistTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("users 1 item")
                .available(false)
                .requestId(100L)
                .build();

        assertThrows(EntityNotExistException.class, () -> itemService.create(incomeDto, 1L));
    }

    @Test
    @Order(16)
    void createUserNotExistTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("users 1 item")
                .available(false)
                .build();

        assertThrows(EntityNotExistException.class, () -> itemService.create(incomeDto, 100L));
    }

    @Test
    @Order(17)
    void deleteByIdTest() {
        itemService.deleteById(1L, 1L);
        assertThrows(EntityNotExistException.class, () -> itemService.getById(1L, 1L));
    }
}
