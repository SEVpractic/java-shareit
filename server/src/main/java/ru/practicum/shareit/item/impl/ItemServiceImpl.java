package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.CreationErrorException;
import ru.practicum.shareit.util.exceptions.EntityNotExistException;
import ru.practicum.shareit.util.exceptions.UpdateErrorException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto getById(long itemId, long userId) {
        Item item = findById(itemId);

        if (item.getOwner().getId() == userId) {
            log.info("Возвращена вещь c id = {} для владельца", itemId);
            return ItemMapper.itemDtoForOwner(
                    item,
                    findNearestBookings(List.of(item)).getOrDefault(item, List.of()),
                    getByItemId(itemId));
        }

        log.info("Возвращена вещь c id = {} для пользователя", itemId);
        return ItemMapper.toItemDto(item, getByItemId(itemId));
    }

    @Override
    public List<ItemDto> getAllByUserId(int from, int size, long userId) {
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size
        );

        List<Item> items = itemRepository.findAllByOwner_IdOrderById(userId, pageable);
        Map<Item, List<Booking>> bookings = findNearestBookings(items);
        Map<Item, List<Comment>> comments = findComments(items);

        log.info("Возвращен список всех вещей пользователе с id = {}", userId);
        return ItemMapper.itemDtoForOwner(items, bookings, comments);
    }

    @Override
    public List<ItemDto> getAllByText(int from, int size, String text) {
        if (text.isBlank()) return List.of();
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size),
                size
        );

        text = text.trim().toLowerCase();
        List<Item> items = itemRepository.findByText(text, pageable);
        Map<Item, List<Comment>> comments = findComments(items);

        log.info("Возвращен список всех вещей со словом \"{}\" в названии или описании", text);
        return ItemMapper.toItemDto(items, comments);
    }

    @Override
    public List<Comment> getByItemId(Long itemId) {
        log.info("Возвращен сипок коментарием для вещи c id = {} ", itemId);
        return commentRepository.findAllByItem_Id(itemId);
    }

    @Override
    @Transactional
    public ItemDto create(ItemIncomeDto itemDto, long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(findUserById(userId));
        if (itemDto.getRequestId() != null) {
            item.setItemRequest(findItemRequestById(itemDto.getRequestId()));
        }

        item = itemRepository.save(item);
        log.info("Создана вещь c id = {} ", item.getId());
        return ItemMapper.toItemDto(item, getByItemId(item.getId()));
    }

    @Override
    @Transactional
    public ItemDto update(ItemIncomeDto itemDto, Long itemId, Long userId) {
        Item item = findById(itemId);
        ownerIdCheck(item, userId);
        update(item, itemDto);

        log.info("Обновлена вещь c id = {} ", item.getId());
        return ItemMapper.toItemDto(item, List.of());
    }

    @Override
    @Transactional
    public void deleteById(long itemId, long userId) {
        itemRepository.deleteItemByIdAndOwner_Id(itemId, userId);
        log.info("Удалена вещь c id = {} ", itemId);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto, long itemId, long userId) {
        checkItemBooking(itemId, userId);

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(findUserById(userId));
        comment.setItem(findById(itemId));

        log.info("Добавлен комментиарий вещи c id = {} от пользователя с id = {}",
                comment.getItem().getId(), comment.getAuthor().getId());
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public void deleteAll() {
        itemRepository.deleteAll();
        log.info("Удалены все вещи");
    }

    private void ownerIdCheck(Item item, long userId) {
        long ownersId = item.getOwner().getId();

        if (ownersId != userId) {
            log.info("Ошибка обновления вещи c id = {} ", item.getId());
            throw new UpdateErrorException(
                    String.format(
                            "Ошибка обновления вещи c id = %s , невозможно обновить вещь другого пользователя",
                            item.getId()
                    )
            );
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new EntityNotExistException(
                                String.format("Пользователь c id = %s не существует", userId))
                );
    }

    private Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotExistException(
                        String.format("Вещь c id = %s не существует", itemId))
                );
    }

    private Map<Item, List<Comment>> findComments(List<Item> items) {
        return commentRepository.findByItemIn(items)
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem));
    }

    private Map<Item, List<Booking>> findNearestBookings(List<Item> items) {
        return bookingRepository.findNearlyBookingByItemIn(items)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
    }

    private void checkItemBooking(Long itemId, Long bookerId) {
        if (bookingRepository.findToCheck(itemId, bookerId) < 1) {
            throw new CreationErrorException("Оставлять отзыв может только пользователь, использовавший вещь");
        }
    }

    private void update(Item item, ItemIncomeDto itemDto) {
        if (itemDto.getDescription() != null &&
                !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null &&
                !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }

    private ItemRequest findItemRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(
                        () -> new EntityNotExistException(
                                String.format("Запрос c id = %s не существует", requestId)));
    }
}
