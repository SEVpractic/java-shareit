package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.util.validation.EndAfterStartValidation;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@EndAfterStartValidation
public class BookingIncomeDto {
    @NotNull
    private Long itemId;
    @FutureOrPresent
    private final LocalDateTime start;
    @Future
    private final LocalDateTime end;
}
