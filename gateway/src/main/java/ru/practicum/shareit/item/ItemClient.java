package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getById(long itemId, long userId) {
        log.info("Обработка запроса на возврат вещи c id = {} ", itemId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllByUserId(int from, int size, long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );

        log.info("Обработка запроса на возврат вещей по userId = {} ", userId);
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByText(int from, int size, String text) {
        if (text.isBlank()) return ResponseEntity.ok().body(List.of());

        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );

        log.info("Обработка запроса на возврат вещей по тексту = {} ", text);
        return get("/search?from={from}&size={size}&text={text}", null, parameters);
    }

    public ResponseEntity<Object> create(ItemIncomeDto itemDto, long userId) {
        log.info("Обработка запроса создания вещи пользователя c id = {} ", userId);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> addComment(CommentDto commentDto, long itemId, long userId) {
        log.info("Обработка запроса добавления комментария пользователя c id = {} ", userId);
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> update(ItemIncomeDto itemDto, long itemId, long userId) {
        log.info("Обработка запроса обновления вещи пользователя c id = {} ", userId);
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> deleteById(long itemId, long userId) {
        log.info("Обработка запроса на удаление вещи c id = {} ", itemId);
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> deleteAll() {
        log.info("Обработка запроса на удаление всех вещей");
        return delete("");
    }
}
