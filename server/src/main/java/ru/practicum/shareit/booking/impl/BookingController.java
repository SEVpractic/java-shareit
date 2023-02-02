package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingIncomeDto bookingIncomeDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.create(bookingIncomeDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirm(@PathVariable("bookingId") long id,
                              @RequestParam(name = "approved") boolean approved,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.confirm(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable("bookingId") long id,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getById(id, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(name = "state") BookingState state,
                                           @RequestParam int from,
                                           @RequestParam int size,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllByBooker(from, size, state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(name = "state") BookingState state,
                                          @RequestParam int from,
                                          @RequestParam int size,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllByOwner(from, size, state, userId);
    }
}
