package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;
import ru.practicum.shareit.item.impl.ItemController;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final ItemService itemService;

    @SneakyThrows
    @Test
    void getById_correctUser_thenReturnOk() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemService).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getById_unCorrectUser_thenReturnBadRequest() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(itemService, Mockito.never()).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByUserId_withPaginationParams_thenReturnOk() {
        long userId = 1L;
        int from = 3;
        int size = 2;
        mockMvc.perform(MockMvcRequestBuilders.get("/items?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemService).getAllByUserId(3, 2, userId);
    }

    @SneakyThrows
    @Test
    void create_allCorrect_thenReturnOk() {
        long userId = 1L;
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("item description")
                .available(false)
                .requestId(null)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("item description")
                .available(false)
                .owner(UserDto.builder()
                        .id(1L)
                        .name("user")
                        .email("user@yandex.ru")
                        .build())
                .nextBooking(null)
                .lastBooking(null)
                .comments(List.of())
                .requestId(null)
                .build();

        Mockito.when(itemService.create(any(), anyLong())).thenReturn(itemDto);
        String content = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemDto), content);
    }

    @SneakyThrows
    @Test
    void update_allCorrect_thenReturnOk() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("item")
                .description("item description")
                .available(true)
                .requestId(null)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("item description")
                .available(true)
                .owner(UserDto.builder()
                        .id(1L)
                        .name("user")
                        .email("user@yandex.ru")
                        .build())
                .nextBooking(null)
                .lastBooking(null)
                .comments(List.of())
                .requestId(null)
                .build();

        Mockito.when(itemService.update(any(), anyLong(), anyLong())).thenReturn(itemDto);
        String content = mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(itemDto), content);
    }

    @SneakyThrows
    @Test
    void addComment_allCorrect_thenReturnOk() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = CommentDto.builder()
                .authorName("user")
                .text("not bad")
                .build();

        Mockito.when(itemService.addComment(any(), anyLong(), anyLong())).thenReturn(commentDto);
        String content = mockMvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(commentDto), content);
    }

    @SneakyThrows
    @Test
    void deleteById_correctUser_thenReturnOk() {
        long userId = 1L;
        long itemId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemService).deleteById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void deleteAll_thenReturnOk() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemService).deleteAll();
    }
}
