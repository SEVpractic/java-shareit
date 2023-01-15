package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ItemRequestDtoForOwner {
    private final Long id;
    private final String description;
    private final LocalDateTime created;
    private final List<ShortItemResponseDto> responses;

    @Builder(toBuilder = true)
    @Getter
    public static class ShortItemResponseDto {
        @JsonProperty(value = "itemId")
        private final long itemId;
        @JsonProperty(value = "name")
        private final String itemName;
        @JsonProperty(value = "ownersId")
        private final Long ownersId;
    }
}
