package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.validation.EndAfterStartValidation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@EndAfterStartValidation
public class BookingDto {
    private final Long id;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime end;
    private final ItemDto item;
    private final UserDto booker;
    private Long itemId;
    private BookingStatus status;

    @JsonIgnore
    public Long getItemId() {
        return itemId;
    }

    @JsonProperty
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}