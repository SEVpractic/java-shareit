package ru.practicum.shareit.item.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.BookingPair;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.impl.UserMapper;

@UtilityClass
public class ItemMapper {

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .build();
    }

    public static ItemDto itemDtoForOwner(Item item, BookingPair pair) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .lastBooking(pair.getLastBooking() == null ? null : new ShortBookingDto(
                        pair.getLastBooking().getId(),
                        pair.getLastBooking().getBooker().getId()
                ))
                .nextBooking(pair.getNextBooking() == null ? null : new ShortBookingDto(
                        pair.getNextBooking().getId(),
                        pair.getNextBooking().getBooker().getId()
                ))
                .build();
    }
}
