package ru.practicum.shareit.booking;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.impl.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

class BookingMapperTest {

    @Test
    void toBookingTest() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        BookingIncomeDto incomeDto = BookingIncomeDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        Booking booking = BookingMapper.toBooking(incomeDto);

        Assertions.assertThat(booking)
                .hasFieldOrPropertyWithValue("id", null)
                .hasFieldOrPropertyWithValue("start", start)
                .hasFieldOrPropertyWithValue("end", end);
    }

    @Test
    void toBookingDtoTest() {
        Booking booking = fillEntity();

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);

        Assertions.assertThat(bookingDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("start", booking.getStart())
                .hasFieldOrPropertyWithValue("end", booking.getEnd())
                .hasFieldOrProperty("item")
                .hasFieldOrProperty("booker")
                .hasFieldOrPropertyWithValue("status", BookingStatus.APPROVED);
        Assertions.assertThat(bookingDto.getItem())
                .hasFieldOrPropertyWithValue("itemId", 1L)
                .hasFieldOrPropertyWithValue("itemName", "itemName");
        Assertions.assertThat(bookingDto.getBooker())
                .hasFieldOrPropertyWithValue("bookerId", 1L)
                .hasFieldOrPropertyWithValue("bookerName", "userName");
    }

    @Test
    void toBookingDtosTest() {
        List<Booking> bookings = List.of(fillEntity());

        List<BookingDto> bookingDtos = BookingMapper.toBookingDto(bookings);

        Assertions.assertThat(bookingDtos)
                .hasSize(1);
    }

    private Booking fillEntity() {
        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");

        User user = new User();
        user.setId(1L);
        user.setName("userName");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking.setItem(item);
        booking.setBooker(user);

        return booking;
    }
}
