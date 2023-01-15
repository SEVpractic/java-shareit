package ru.practicum.shareit.request.impl;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDtoForOwner;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static ItemRequestDtoForOwner.ShortItemResponseDto toShortItemResponseDto(Item item) {
        return ItemRequestDtoForOwner.ShortItemResponseDto.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getItemRequest().getId())
                .build();
    }

    public static ItemRequestDtoForOwner toItemRequestDtoForOwner(ItemRequest itemRequest,
                                                                  List<Item> items) {
        return ItemRequestDtoForOwner.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(
                        items.stream()
                                .map(ItemRequestMapper::toShortItemResponseDto)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static List<ItemRequestDtoForOwner> toItemRequestDtoForOwner(List<ItemRequest> itemRequests,
                                                                        Map<ItemRequest, List<Item>> itemsByRequests) {
        return itemRequests.stream()
                .map(itemRequest -> toItemRequestDtoForOwner(
                        itemRequest,
                        itemsByRequests.getOrDefault(itemRequest, List.of())
                ))
                .collect(Collectors.toList());
    }
}
