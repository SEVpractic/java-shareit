package ru.practicum.shareit.booking.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.Booking;

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
                .item(new BookingDto.ShortItemDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                ))
                .booker(new BookingDto.ShortBookerDto(
                        booking.getBooker().getId(),
                        booking.getBooker().getName()
                ))
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
