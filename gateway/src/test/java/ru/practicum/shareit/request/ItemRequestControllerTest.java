package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestIncomeDto;
import ru.practicum.shareit.util.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    RequestClient requestClient;

    @SneakyThrows
    @Test
    void getById_allCorrect_thenReturnOk() {
        long userId = 1L;
        long requestId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestClient).getById(requestId, userId);
    }

    @SneakyThrows
    @Test
    void getAll_withoutStateAndPagination_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestClient).getAll(0, 10, 1L);
    }

    @SneakyThrows
    @Test
    void getAll_notWalidPagination_thenReturnOk() {
        long userId = 1L;
        int from = -1;
        int size = 0;
        mockMvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(requestClient, never()).getAll(0, 10, 1L);
    }

    @SneakyThrows
    @Test
    void getForOwner_allCorrect_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(requestClient).getForOwner(userId);
    }

    @SneakyThrows
    @Test
    void create_allCorrect_thenReturnOk() {
        long userId = 1L;
        ItemRequestIncomeDto incomeDto = ItemRequestIncomeDto.builder()
                .description("text")
                .build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("text")
                .created(LocalDateTime.now().plusHours(1))
                .requestor(new ItemRequestDto.ShortRequestorDto(1L, "user"))
                .build();
        String requestJson = objectMapper.writeValueAsString(itemRequestDto);

        ResponseEntity<Object> response = new ResponseEntity<>(requestJson, HttpStatus.OK);

        when(requestClient.create(any(), anyLong())).thenReturn(response);
        String content = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(requestJson, content);
    }
}
