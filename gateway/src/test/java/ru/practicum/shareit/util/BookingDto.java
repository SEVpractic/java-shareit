package ru.practicum.shareit.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class BookingDto {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final ShortItemDto item;
    private final ShortBookerDto booker;
    private BookingStatus status;

    @RequiredArgsConstructor
    @Getter
    public static class ShortBookerDto {
        @JsonProperty(value = "id")
        private final long bookerId;
        @JsonProperty(value = "name")
        private final String bookerName;
    }

    @RequiredArgsConstructor
    @Getter
    public static class ShortItemDto {
        @JsonProperty(value = "id")
        private final long itemId;
        @JsonProperty(value = "name")
        private final String itemName;
    }
}