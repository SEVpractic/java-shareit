package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserIncomeDto;
import ru.practicum.shareit.util.UserDto;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final UserClient userClient;

    @SneakyThrows
    @Test
    void getById_correctUser_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).getById(userId);
    }

    @SneakyThrows
    @Test
    void getAll_correctUser_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/users", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).getAll();
    }

    @SneakyThrows
    @Test
    void createTest_correctUserIncomeDto_thenReturnOk() {
        UserIncomeDto userIncomeDto = UserIncomeDto.builder()
                .name("User")
                .email("user@yandex.ru")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@yandex.ru")
                .build();
        String userJson = objectMapper.writeValueAsString(userDto);

        ResponseEntity<Object> response = new ResponseEntity<>(userJson, HttpStatus.OK);

        when(userClient.create(any())).thenReturn(response);
        String result = mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(userJson, result);
    }

    @SneakyThrows
    @Test
    void createTest_unCorrectUserEmail_thenReturnBadRequest() {
        UserIncomeDto userIncomeDto = UserIncomeDto.builder()
                .name("User")
                .email("userYandexRu")
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void createTest_UnCorrectUserName_thenReturnBadRequest() {
        UserIncomeDto userIncomeDto = UserIncomeDto.builder()
                .name(null)
                .email("user@yandex.ru")
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userClient, never()).create(any());
    }

    @SneakyThrows
    @Test
    void updateTest_correctUserIncomeDto_thenReturnOk() {
        UserIncomeDto userIncomeDto = UserIncomeDto.builder()
                .name("User")
                .email("user@yandex.ru")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@yandex.ru")
                .build();
        String userJson = objectMapper.writeValueAsString(userDto);

        ResponseEntity<Object> response = new ResponseEntity<>(userJson, HttpStatus.OK);

        when(userClient.update(any(), anyLong())).thenReturn(response);
        String result = mockMvc.perform(
                        patch("/users/{userId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(userJson, result);
    }

    @SneakyThrows
    @Test
    void deleteById_correctUser_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(delete("/users/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).deleteById(userId);
    }

    @SneakyThrows
    @Test
    void deleteAll_thenReturnOk() {
        mockMvc.perform(delete("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userClient).deleteAll();
    }

    @SneakyThrows
    @Test
    void createTest_emailAlreadyExist_thenReturnBadRequest() {
        UserIncomeDto userIncomeDto = UserIncomeDto.builder()
                .name("User")
                .email("user@yandex.ru")
                .build();

        when(userClient.create(any())).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userIncomeDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
