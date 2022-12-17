package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.CreateValidationGroup;
import ru.practicum.shareit.util.ItemMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable("itemId") @Positive long id) {
        Item item = itemService.getById(id);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getAllByUserId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam(name = "text") @NotNull String text) {
        return itemService.getAllByText(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto create(@Validated(CreateValidationGroup.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        itemDto = itemDto.toBuilder().owner(User.builder().id(userId).build()).build();
        Item item = itemService.create(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") @Positive long id,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        itemDto = itemDto.toBuilder().id(id).owner(User.builder().id(userId).build()).build();
        Item item = itemService.update(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") @Positive long id) {
        itemService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }
}
