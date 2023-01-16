package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.model.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingIncomeDto bookingIncomeDto,
                             @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return bookingService.create(bookingIncomeDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirm(@PathVariable("bookingId") @Positive long id,
                              @RequestParam(name = "approved") boolean approved,
                              @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return bookingService.confirm(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable("bookingId") @Positive long id,
                              @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return bookingService.getById(id, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size,
                                           @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return bookingService.getAllByBooker(from, size, state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size,
                                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return bookingService.getAllByOwner(from, size, state, userId);
    }

}
