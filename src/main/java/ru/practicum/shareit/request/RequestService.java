package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;

public interface RequestService {
    ItemRequestDto create(ItemRequestIncomeDto requestDto, long userId);

    ItemRequestDto getById(long requestId, long userId);
}
