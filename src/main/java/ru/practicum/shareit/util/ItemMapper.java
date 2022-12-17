package ru.practicum.shareit.util;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    private ItemMapper() {
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = Item.builder().owner(itemDto.getOwner()).build();

        if (itemDto.getRequest() != null) {
            item = item.toBuilder().request(itemDto.getRequest()).build();
        }
        if (itemDto.getAvailable() != null) {
            item = item.toBuilder().available(itemDto.getAvailable()).build();
        }
        if (itemDto.getDescription() != null) {
            item = item.toBuilder().description(itemDto.getDescription()).build();
        }
        if (itemDto.getName() != null) {
            item = item.toBuilder().name(itemDto.getName()).build();
        }
        if (itemDto.getId() != null) {
            item = item.toBuilder().id(itemDto.getId()).build();
        }

        return item;
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }
}
