package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.util.BookingDto;
import ru.practicum.shareit.util.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    @MockBean
    private BookingClient bookingClient;

    @SneakyThrows
    @Test
    void getById_correctUser_thenReturnOk() {
        long userId = 1L;
        long bookingId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient).getById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllByBooker_withoutStateAndPagination_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient).getAllByBooker(0, 10, BookingState.ALL, 1L);
    }

    @SneakyThrows
    @Test
    void getAllByBooker_notWalidPagination_thenReturnOk() {
        long userId = 1L;
        int from = -1;
        int size = 0;
        mockMvc.perform(get("/bookings?from={from}&size={size}", from, size)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).getAllByBooker(0, 10, BookingState.ALL, 1L);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_WAITING_thenReturnBadRequest() {
        long userId = 1L;
        BookingState state = BookingState.WAITING;
        mockMvc.perform(get("/bookings/owner?state={state}", state)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient).getAllByOwner(0, 10, BookingState.WAITING, 1L);
    }

    @SneakyThrows
    @Test
    void create_allCorrect_thenReturnOk() {
        long userId = 1L;
        BookingIncomeDto bookingIncomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .booker(new BookingDto.ShortBookerDto(1L, "user"))
                .item(new BookingDto.ShortItemDto(1L, "item"))
                .status(BookingStatus.WAITING)
                .build();
        String bookingJson = objectMapper.writeValueAsString(bookingDto);

        ResponseEntity<Object> response = new ResponseEntity<>(bookingJson, HttpStatus.OK);

        when(bookingClient.create(any(), anyLong())).thenReturn(response);
        String content = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingIncomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertEquals(bookingJson, content);
    }

    @SneakyThrows
    @Test
    void create_StartAfterEnd_thenReturnBadRequest() {
        long userId = 1L;
        BookingIncomeDto bookingIncomeDto = BookingIncomeDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusMinutes(30))
                .build();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingIncomeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @SneakyThrows
    @Test
    void confirm_allCorrect_thenReturnOk() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient).confirm(bookingId, userId, approved);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_UNSUPPORTED_STATUS_thenReturnBadRequest() {
        long userId = 1L;
        String state = "abracadabra";
        mockMvc.perform(get("/bookings/owner?state={state}", state)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print());

        verify(bookingClient).getAllByOwner(0, 10, BookingState.UNSUPPORTED_STATUS, 1L);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_unsupported_thenReturnBadRequest() {
        long userId = 1L;
        String state = "blablabla";
        mockMvc.perform(get("/bookings/owner?state={state}", state)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient, never()).getAllByBooker(0, 10, BookingState.ALL, 1L);
    }

    @SneakyThrows
    @Test
    void getAllByBooker_unsupported_calRejectedQuery() {
        long userId = 1L;
        String state = "blablabla";
        mockMvc.perform(get("/bookings?state={state}", state)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingClient, never()).getAllByBooker(0, 10, BookingState.ALL, 1L);
    }
}
