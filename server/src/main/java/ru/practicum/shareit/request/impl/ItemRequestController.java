package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.request.dto.ItemRequestLongDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestIncomeDto requestDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestLongDto> getForOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getForOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestLongDto> getAll(@RequestParam int from,
                                           @RequestParam int size,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestLongDto getById(@PathVariable("requestId") long requestId,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getById(requestId, userId);
    }
}
