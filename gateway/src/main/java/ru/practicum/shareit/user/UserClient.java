package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserIncomeDto;

@Service
@Slf4j
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAll() {
        log.info("Обработка запроса на возврат всех пользователей");
        return get("");
    }

    public ResponseEntity<Object> getById(long userId) {
        log.info("Обработка запроса на возврат пользователя c id = {} ", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> create(UserIncomeDto userDto) {
        log.info("Обработка запроса создания пользователя c name = {} ", userDto.getName());
        return post("", userDto);
    }

    public ResponseEntity<Object> update(UserIncomeDto userDto, long userId) {
        log.info("Обработка запроса обновления пользователя c id = {} ", userId);
        return patch("/" + userId, userDto);
    }

    public void deleteById(long userId) {
        log.info("Обработка запроса на удаление пользователя c id = {} ", userId);
        delete("/" + userId);
    }

    public void deleteAll() {
        log.info("Обработка запроса на удаление всех пользователей");
        delete("");
    }
}
