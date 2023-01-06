package ru.practicum.shareit.booking.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.impl.ItemMapper;
import ru.practicum.shareit.user.impl.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(BookingIncomeDto bookingIncomeDto) {
        Booking booking = new Booking();

        booking.setStart(bookingIncomeDto.getStart());
        booking.setEnd(bookingIncomeDto.getEnd());

        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem(), List.of()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking))
                .collect(Collectors.toList());
    }
}
