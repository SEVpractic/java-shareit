package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final ItemService itemService;

    @Test
    @Order(0)
    @Sql(value = { "/test-schema.sql", "/users-create-test.sql" })
    void createTest() {
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

    @Test
    @Order(1)
    void updateAvailableTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .available(true)
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(2)
    void updateDescriptionTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .description("users 1 updated item")
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
                });
    }

    @Test
    @Order(3)
    void updateNameTest() {
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("updated item")
                .build();
        Optional<ItemDto> itemDto = Optional.of(itemService.update(incomeDto, 1L, 1L));

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
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

        assertThat(exception)
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
    @Sql(value = { "/bookings-create-test.sql" })
    void addCommentTest() {
        CommentDto incomeCommentDto = CommentDto.builder()
                .text("text 1 comment")
                .authorName("comment 1 authorName")
                .build();
        Optional<CommentDto> commentDto = Optional.of(itemService.addComment(incomeCommentDto, 1L, 2L));

        assertThat(commentDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("text", "text 1 comment");
                    assertThat(i).hasFieldOrPropertyWithValue("authorName", "name1");
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

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    assertThat(i).hasFieldOrProperty("lastBooking");
                    assertThat(i.getLastBooking()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    assertThat(i).hasFieldOrProperty("nextBooking");
                    assertThat(i.getNextBooking()).isNull();
                    assertThat(i).hasFieldOrProperty("comments");
                    assertThat(i.getComments()).hasSize(1);
                });
    }

    @Test
    @Order(9)
    void getByIdNotForOwnerTest() {
        Optional<ItemDto> itemDto = Optional.of(itemService.getById(1L, 3L));

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 1 updated item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    assertThat(i.getLastBooking()).isNull();
                    assertThat(i).hasFieldOrProperty("comments");
                    assertThat(i.getComments()).hasSize(1);
                });
    }

    @Test
    @Order(10)
    void getAllByOwnerIdTest() {
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);
        assertThat(items)
                .hasSize(1);

        items = itemService.getAllByUserId(0, 10, 3L);
        assertThat(items)
                .isEmpty();
    }

    @Test
    @Order(11)
    void getAllByTextTest() {
        List<ItemDto> items = itemService.getAllByText(0, 10, "item");

        assertThat(items)
                .hasSize(1);

        assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(12)
    @Sql(value = { "/item-create-test.sql" })
    void getAllByUserIdPaginationTest() {
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);

        assertThat(items)
                .hasSize(6);
        assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "updated item");

        items = itemService.getAllByUserId(3, 2, 1L);

        assertThat(items)
                .hasSize(2);
        assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("name", "item2");
    }

    @Test
    @Order(13)
    void deleteAll() {
        itemService.deleteAll();
        List<ItemDto> items = itemService.getAllByUserId(0, 10, 1L);

        assertThat(items)
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

        assertThat(itemDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 7L);
                    assertThat(i).hasFieldOrPropertyWithValue("name", "item");
                    assertThat(i).hasFieldOrPropertyWithValue("description", "users 3 item");
                    assertThat(i).hasFieldOrPropertyWithValue("available", true);
                    assertThat(i).hasFieldOrPropertyWithValue("requestId", 1L);
                });
    }


}
