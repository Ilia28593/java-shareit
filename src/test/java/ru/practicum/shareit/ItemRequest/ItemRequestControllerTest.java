package ru.practicum.shareit.ItemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Samples;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.config.Constants;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    @MockBean
    private final ItemRequestService itemRequestService;
    private final ItemDto itemDto = Samples.getItem1();
    private final ItemRequestResponseDto itemRequestResponseDto =
            Samples.getItemRequestResponseDto(1L, List.of(itemDto), LocalDateTime.now());

    private final ItemRequestDto itemRequestDto = Samples.getItemRequestDto();

    @Test
    public void create() throws Exception {
        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(itemRequestResponseDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.items[0]", is(itemRequestResponseDto.getItems().iterator().next()), ItemDto.class));
    }

    @Test
    public void findByUserId() throws Exception {
        when(itemRequestService.findByUserId(anyLong()))
                .thenReturn(List.of(itemRequestResponseDto, itemRequestResponseDto));
        mvc.perform(get("/requests?from=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void findAll() throws Exception {
        when(itemRequestService.findAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(itemRequestResponseDto, itemRequestResponseDto));
        mvc.perform(get("/requests/all?from=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    public void findRequestById() throws Exception {
        when(itemRequestService.findById(anyLong(), anyLong()))
                .thenReturn(itemRequestResponseDto);
        mvc.perform(get("/requests/{requestId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.items[0]", is(itemRequestResponseDto.getItems().iterator().next()), ItemDto.class));
    }
}
