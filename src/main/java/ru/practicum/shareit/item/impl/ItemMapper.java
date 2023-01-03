package ru.practicum.shareit.item.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.impl.UserMapper;

import java.util.List;

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

    public static ItemDto itemDtoForOwner(Item item, List<Booking> bookings) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .lastBooking(bookings.get(0) == null ? null : new BookingShortDto(
                        bookings.get(0).getId(),
                        bookings.get(0).getBooker().getId()
                ))
                .nextBooking(bookings.get(1) == null ? null : new BookingShortDto(
                        bookings.get(1).getId(),
                        bookings.get(1).getBooker().getId()
                ))
                .build();
    }
}