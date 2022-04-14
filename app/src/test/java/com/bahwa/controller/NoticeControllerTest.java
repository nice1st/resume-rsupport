package com.bahwa.controller;

import com.bahwa.constants.NoticeConstants;
import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.ControllerExceptionHandler;
import com.bahwa.gson.GsonLocalDateTimeAdapter;
import com.bahwa.service.NoticeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class NoticeControllerTest {

    @InjectMocks
    private NoticeController target;

    @Mock
    private NoticeService noticeService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
        mockMvc = MockMvcBuilders.standaloneSetup(target).setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    private final LocalDateTime now = LocalDateTime.now();
    private final Long id = 1L;
    private final String writer = "user1";
    private final String title = "title1";
    private final String contents = "contents1234567890";

    @Test
    public void 등록성공() throws Exception {
        
        String url = "/api/v1/notices";
        
        doReturn(Notice.builder().id(id).title(title).contents(contents).periodStart(now).build()).when(noticeService).addNotice(any(NoticeDto.class));

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            .periodStart(now)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void 등록실패_헤더에_작성자_없음() throws Exception {
        
        String url = "/api/v1/notices";

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            .periodStart(now)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                // .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 등록실패_제목_없음() throws Exception {

        String url = "/api/v1/notices";

        NoticeDto dto = NoticeDto.builder()
            // .title(title)
            .contents(contents)
            .periodStart(now)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 등록실패_내용_없음() throws Exception {

        String url = "/api/v1/notices";

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            // .contents(contents)
            .periodStart(now)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 등록실패_시작일_없음() throws Exception {

        String url = "/api/v1/notices";

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            // .periodStart(now)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }
    
    @Test
    public void 등록실패_period() throws Exception {

        String url = "/api/v1/notices";

        LocalDateTime errPeriodStart = now;
        LocalDateTime errPeriodEnd = now.minusDays(1L);

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            .periodStart(errPeriodStart)
            .periodEnd(errPeriodEnd)
            .build();
    
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }
}
