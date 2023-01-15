package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.util.validation.CreateValidationGroup;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto create(@Validated(CreateValidationGroup.class) @RequestBody ItemRequestIncomeDto requestDto,
                                 @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoForOwner> getForOwner(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestService.getForOwner(userId);
    }

    /*@GetMapping("/all")
    public List<ItemRequestDto> getAll(@PathVariable("from") @Positive long from,
                                       @PathVariable("size") @Positive long size,
                                       @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return null;
    }*/

    @GetMapping("{requestId}")
    public ItemRequestDtoForOwner getById(@PathVariable("requestId") @Positive long requestId,
                                  @RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return requestService.getById(requestId, userId);
    }
}
