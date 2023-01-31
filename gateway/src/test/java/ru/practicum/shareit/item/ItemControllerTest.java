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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemIncomeDto;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private final ItemClient itemClient;

    @SneakyThrows
    @Test
    void getById_correctUser_thenReturnOk() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemClient).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getById_unCorrectUser_thenReturnBadRequest() {
        long itemId = 1L;
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(itemClient, Mockito.never()).getById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByUserId_withoutPaginationParams_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemClient).getAllByUserId(0, 10, userId);
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

        Mockito.verify(itemClient).getAllByUserId(3, 2, userId);
    }

    @SneakyThrows
    @Test
    void getAllByText_withoutPaginationParams_thenReturnOk() {
        String text = "java forever";
        mockMvc.perform(MockMvcRequestBuilders.get("/items/search?text={text}", text))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemClient).getAllByText(0, 10, text);
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

        String itemJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"item\",\n" +
                "    \"description\": \"item description\",\n" +
                "    \"available\": false),\n" +
                "    \"owner\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"user\",\n" +
                "        \"email\": \"user@yandex.ru\"\n" +
                "    },\n" +
                "    \"lastBooking\": null,\n" +
                "    \"nextBooking\": null,\n" +
                "    \"comments\": [],\n" +
                "    \"requestId\": null\n" +
                "}";
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        Mockito.when(itemClient.create(any(), anyLong())).thenReturn(response);
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

        Assertions.assertEquals(itemJson, content);
    }

    @SneakyThrows
    @Test
    void create_unCorrectItemDto_thenReturnOk() {
        long userId = 1L;
        ItemIncomeDto incomeDto = ItemIncomeDto.builder()
                .name("")
                .description("")
                .available(false)
                .requestId(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(itemClient, Mockito.never()).create(any(), anyLong());
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

        String itemJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"item\",\n" +
                "    \"description\": \"item description\",\n" +
                "    \"available\": false),\n" +
                "    \"owner\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"user\",\n" +
                "        \"email\": \"user@yandex.ru\"\n" +
                "    },\n" +
                "    \"lastBooking\": null,\n" +
                "    \"nextBooking\": null,\n" +
                "    \"comments\": [],\n" +
                "    \"requestId\": null\n" +
                "}";
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        Mockito.when(itemClient.update(any(), anyLong(), anyLong())).thenReturn(response);
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

        Assertions.assertEquals(itemJson, content);
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
        String commentJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"text\": \"not bad\",\n" +
                "    \"authorName\": \"user\"\n" +
                "}";
        ResponseEntity<Object> response = new ResponseEntity<>(commentJson, HttpStatus.OK);

        Mockito.when(itemClient.addComment(any(), anyLong(), anyLong())).thenReturn(response);
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

        Assertions.assertEquals(commentJson, content);
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

        Mockito.verify(itemClient).deleteById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void deleteAll_thenReturnOk() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(itemClient).deleteAll();
    }
}
