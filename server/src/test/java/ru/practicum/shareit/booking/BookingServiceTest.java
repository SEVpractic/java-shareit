package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.util.exceptions.BookingPatchException;
import ru.practicum.shareit.util.exceptions.CreationErrorException;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UserNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    private final BookingService bookingService;

    @Test
    @Order(0)
    @Sql(value = {"/test-schema.sql", "/users-create-test.sql", "/item-create-test.sql" })
    void createTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        Optional<BookingDto> bookingDto = Optional.of(bookingService.create(incomeDto, 2L));

        assertThat(bookingDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1L);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    assertThat(i).hasFieldOrPropertyWithValue("status", BookingStatus.WAITING);
                });
    }

    @Test
    @Order(1)
    void confirmTest() {
        Optional<BookingDto> bookingDto = Optional.of(bookingService.confirm(1L, 1L, true));

        assertThat(bookingDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1L);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    assertThat(i).hasFieldOrPropertyWithValue("status", BookingStatus.APPROVED);
                });
    }

    @Test
    @Order(2)
    void getByIdTest() {
        Optional<BookingDto> bookingDto = Optional.of(bookingService.getById(1L, 2L));

        assertThat(bookingDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1L);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    assertThat(i).hasFieldOrPropertyWithValue("status", BookingStatus.APPROVED);
                });
    }

    @Test
    @Order(3)
    void getByIdFailTest() {
        assertThrows(UserNotValidException.class, () -> bookingService.getById(1L, 3L));
    }

    @Test
    @Order(4)
    void rejectFailTest() {
        assertThrows(BookingPatchException.class, () -> bookingService.confirm(1L, 1L, true));
    }

    @Test
    @Order(5)
    void getByIdNotExistTest() {
        assertThrows(EntityNotExistException.class, () -> bookingService.getById(10L, 1L));
    }

    @Test
    @Order(6)
    void createFailTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(CreationErrorException.class, () -> bookingService.create(incomeDto, 2L));
    }

    @Test
    @Order(7)
    void rejectTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(3L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        bookingService.create(incomeDto, 2L);

        Optional<BookingDto> bookingDto = Optional.of(bookingService.confirm(2L, 1L, false));

        assertThat(bookingDto)
                .isPresent()
                .hasValueSatisfying(i -> {
                    assertThat(i).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 3L);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2L);
                    assertThat(i).hasFieldOrPropertyWithValue("status", BookingStatus.REJECTED);
                });
    }

    @Test
    @Order(8)
    void createWithNotExistItemTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(100L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(EntityNotExistException.class, () -> bookingService.create(incomeDto, 2L));
    }

    @Test
    @Order(8)
    void createWithNotExistUserTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(EntityNotExistException.class, () -> bookingService.create(incomeDto, 200L));
    }

    @Test
    @Order(9)
    @Sql(value = {"/all-bookings-create-test.sql"})
    void confirmNotByOwnerTest() {
        assertThrows(UserNotValidException.class, () -> bookingService.confirm(3L, 2L, true));
    }

    @Test
    @Order(10)
    void createFromUserTest() {
        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(UserNotValidException.class, () -> bookingService.create(incomeDto, 1L));
    }

    @Test
    @Order(11)
    void getAllByOwnerTest() {
        int from = 0;
        int size = 10;
        BookingState state = BookingState.ALL;
        long userId = 1L;
        List<BookingDto> bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(5);

        state = BookingState.CURRENT;
        bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .isEmpty();

        state = BookingState.PAST;
        bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(1);

        state = BookingState.FUTURE;
        bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(4);

        state = BookingState.WAITING;
        bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(1);

        state = BookingState.REJECTED;
        bookings = bookingService.getAllByOwner(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(2);
    }

    @Test
    @Order(11)
    void getAllByBookerTest() {
        int from = 0;
        int size = 10;
        BookingState state = BookingState.ALL;
        long userId = 2L;
        List<BookingDto> bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(5);

        state = BookingState.CURRENT;
        bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .isEmpty();

        state = BookingState.PAST;
        bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(1);

        state = BookingState.FUTURE;
        bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(4);

        state = BookingState.WAITING;
        bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(1);

        state = BookingState.REJECTED;
        bookings = bookingService.getAllByBooker(from, size, state, userId);
        Assertions.assertThat(bookings)
                .hasSize(2);
    }
}
