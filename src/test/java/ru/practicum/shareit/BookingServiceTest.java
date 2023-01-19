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
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.util.exceptions.UserNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingState.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    private final BookingService bookingService;

    @Test
    @Order(0)
    @Sql(value = { "/test-schema.sql", "/users-create-test.sql", "/item-create-test.sql" })
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
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1l);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1l);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2l);
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
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1l);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1l);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2l);
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
                    assertThat(i).hasFieldOrPropertyWithValue("id", 1l);
                    assertThat(i).hasFieldOrProperty("item");
                    assertThat(i.getItem()).hasFieldOrPropertyWithValue("itemId", 1l);
                    assertThat(i).hasFieldOrProperty("booker");
                    assertThat(i.getBooker()).hasFieldOrPropertyWithValue("bookerId", 2l);
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
    @Sql(value = { "/all-bookings-create-test.sql" })
    void getAllByOwnerTest() {
        Optional<BookingDto> bookingDto = Optional.of(bookingService.getById(4L, 2L));


        List<BookingDto> bookings = bookingService.getAllByOwner(0, 10, ALL, 2L);
        assertThat(bookings)
                .hasSize(4);

        bookings = bookingService.getAllByOwner(0, 10, FUTURE, 2L);
        assertThat(bookings)
                .hasSize(3);

        bookings = bookingService.getAllByOwner(0, 10, PAST, 2L);
        assertThat(bookings)
                .hasSize(1);

        bookings = bookingService.getAllByOwner(0, 10, WAITING, 2L);
        assertThat(bookings)
                .hasSize(1);

        bookings = bookingService.getAllByOwner(0, 10, REJECTED, 2L);
        assertThat(bookings)
                .hasSize(1);
    } // todo не работает тест именно в тесте сервисов, переписать
}
