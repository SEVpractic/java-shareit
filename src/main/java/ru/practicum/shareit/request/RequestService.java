package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemRequestDtoForOwner;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto create(ItemRequestIncomeDto requestDto, long userId);

    ItemRequestDto getById(long requestId, long userId);

    List<ItemRequestDtoForOwner> getForOwner(long userId);
}
