package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ItemDto {
    private Long id;
    @NotBlank (groups = CreateValidationGroup.class)
    @Size(max = 50, groups = CreateValidationGroup.class)
    private final String name;
    @NotBlank (groups = CreateValidationGroup.class)
    private final String description;
    @NotNull (groups = CreateValidationGroup.class)
    private final Boolean available;
    private final UserDto owner;
    private final BookingShortDto lastBooking;
    private final BookingShortDto nextBooking;
    private final List<CommentDto> comments;

    public void setId(Long id) {
        this.id = id;
    }
}
