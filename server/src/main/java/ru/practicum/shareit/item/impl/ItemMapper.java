package ru.practicum.shareit.item.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.impl.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static Item toItem(ItemIncomeDto itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemDto toItemDto(Item item, List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .comments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .requestId(item.getItemRequest() == null ? null : item.getItemRequest().getId())
                .build();
    }

    public static List<ItemDto> toItemDto(List<Item> items, Map<Item, List<Comment>> comments) {
        return items.stream()
                .map(item -> ItemMapper.toItemDto(
                                item,
                                comments.getOrDefault(item, List.of()))
                )
                .collect(Collectors.toList());
    }

    public static ItemDto itemDtoForOwner(Item item, List<Booking> bookings, List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .lastBooking(bookings.isEmpty() || bookings.get(0) == null ? null : new BookingShortDto(
                        bookings.get(0).getId(),
                        bookings.get(0).getBooker().getId()
                ))
                .nextBooking(bookings.size() <= 1 || bookings.get(1) == null ? null : new BookingShortDto(
                        bookings.get(1).getId(),
                        bookings.get(1).getBooker().getId()
                ))
                .comments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }

    public static List<ItemDto> itemDtoForOwner(
            List<Item> items,
            Map<Item, List<Booking>> bookings,
            Map<Item, List<Comment>> comments) {
        return items.stream()
                .map(item -> ItemMapper.itemDtoForOwner(
                        item,
                        bookings.getOrDefault(item, List.of()),
                        comments.getOrDefault(item, List.of()))
                )
                .collect(Collectors.toList());
    }
}
