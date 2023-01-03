package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
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
            return ItemMapper.itemDtoForOwner(
                    item,
                    bookingService.findNearest(id),
                    itemService.getByItemId(id));
        }
        return ItemMapper.toItemDto(item, itemService.getByItemId(id));
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getAllByUserId(userId)
                .stream()
                .map(item -> ItemMapper.itemDtoForOwner(
                        item,
                        bookingService.findNearest(item.getId()),
                        itemService.getByItemId(item.getId())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByText(@RequestParam(name = "text") String text) {
        if (text.isBlank()) return List.of();

        return itemService.getAllByText(text)
                .stream()
                .map(item -> ItemMapper.toItemDto(
                        item,
                        itemService.getByItemId(item.getId())
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto create(@Validated(CreateValidationGroup.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.getById(userId));
        item = itemService.create(item);
        return ItemMapper.toItemDto(item, itemService.getByItemId(item.getId()));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") @Positive long id,
                                 @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        bookingService.checkItemBooking(userId, id);

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(itemService.getById(id));
        comment.setAuthor(userService.getById(userId));
        comment = itemService.addComment(comment);

        return CommentMapper.toCommentDto(comment);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") @Positive long id,
                          @RequestHeader("X-Sharer-User-Id") @Positive long userId,
                          @RequestBody ItemDto itemDto) {
        itemDto.setId(id);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.getById(userId));
        item = itemService.update(item);
        return ItemMapper.toItemDto(item, itemService.getByItemId(item.getId()));
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
