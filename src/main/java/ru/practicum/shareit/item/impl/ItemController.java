package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable("itemId") @Positive long id,
                           @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Item item = itemService.getById(id);
        if (item.getOwner().getId() == userId) {
            return ItemMapper.itemDtoForOwner(item, bookingService.findNearest(id));
        }
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getAllByUserId(userId)
                .stream()
                .map(i -> ItemMapper.itemDtoForOwner(i, bookingService.findNearest(i.getId())))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam(name = "text") String text) {
        if (text.isBlank()) return List.of();

        return itemService.getAllByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto create(@Validated(CreateValidationGroup.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.getById(userId));
        item = itemService.create(item);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") @Positive long id,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                          @RequestBody ItemDto itemDto) {
        itemDto.setId(id);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.getById(userId));
        item = itemService.update(item);
        return ItemMapper.toItemDto(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") @Positive long itemId,
                           @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        itemService.deleteById(itemId, userId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }
}
