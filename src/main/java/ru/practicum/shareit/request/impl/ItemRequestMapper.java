package ru.practicum.shareit.request.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestIncomeDto itemRequestDto,
                                            User requestor) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);

        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(new ItemRequestDto.ShortRequestorDto(
                        itemRequest.getRequestor().getId(),
                        itemRequest.getRequestor().getName()
                ))
                .created(itemRequest.getCreated())
                .build();
    }
}
