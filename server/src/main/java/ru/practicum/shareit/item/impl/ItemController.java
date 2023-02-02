package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable("itemId") long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestParam int from,
                                        @RequestParam int size,
                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllByUserId(from, size, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam int from,
                                      @RequestParam int size,
                                      @RequestParam(name = "text") String text) {
        return itemService.getAllByText(from, size, text);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemIncomeDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemIncomeDto itemDto) {
        return itemService.update(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable("itemId") long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteById(itemId, userId);
    }

    @DeleteMapping
    public void deleteAll() {
        itemService.deleteAll();
    }
}
