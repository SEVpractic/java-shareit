package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable("itemId") @Positive long itemId,
                           @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam(name = "text") String text) {
        return itemService.getAllByText(text);
    }

    @PostMapping
    public ItemDto create(@Validated(CreateValidationGroup.class) @RequestBody ItemIncomeDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") @Positive long itemId,
                                 @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") @Positive long itemId,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                          @Valid @RequestBody ItemIncomeDto itemDto) {
        return itemService.update(itemDto, itemId, userId);
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
