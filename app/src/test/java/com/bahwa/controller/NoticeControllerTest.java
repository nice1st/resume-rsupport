package com.bahwa.controller;

import com.bahwa.constants.NoticeConstants;
import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.ControllerExceptionHandler;
import com.bahwa.exception.NoticeException;
import com.bahwa.gson.GsonLocalDateTimeAdapter;
import com.bahwa.service.AttachmentsService;
import com.bahwa.service.NoticeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class NoticeControllerTest {

    @InjectMocks
    private NoticeController target;

    @Mock
    private NoticeService noticeService;

    @Mock
    private AttachmentsService attachmentsService;

    @Spy
    private Executor executor;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
        mockMvc = MockMvcBuilders.standaloneSetup(target).setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    private final String url = "/api/v1/notices";

    private static final LocalDateTime now = LocalDateTime.now();
    private static final Long id = 1L;
    private static final String writer = "user1";
    private static final String title = "title1";
    private static final String contents = "contents1234567890";

    @Test
    public void ????????????() throws Exception {
        
        
        doReturn(Notice.builder().id(id).title(title).contents(contents).periodStart(now).build()).when(noticeService).addNotice(any(NoticeDto.class));
        
        NoticeDto dto = NoticeDto.builder()
        .title(title)
        .contents(contents)
        .periodStart(now)
        .build();

        MockMultipartFile mockfile1 = new MockMultipartFile("file", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
        MockMultipartFile mockfile2 = new MockMultipartFile("file", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("noticeDto", "", "application/json", gson.toJson(dto).getBytes());

        ResultActions resultActions = mockMvc.perform(
            multipart(url)
                .file(mockfile1)
                .file(mockfile2)
                .file(jsonFile)
                .header(NoticeConstants.WRITER_HEADER, writer)
        );
                
        resultActions.andDo(print());
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void ????????????_?????????_?????????_??????() throws Exception {
        
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

    @ParameterizedTest
    @MethodSource("invalidNoticeAddParameter")
    public void ????????????_?????????(String title, String contents, LocalDateTime periodStart, LocalDateTime periodEnd) throws Exception {

        NoticeDto dto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            .periodStart(periodStart)
            .periodEnd(periodEnd)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .content(gson.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidNoticeAddParameter() {

        return Stream.of(
            Arguments.of(null, contents, now, null),
            Arguments.of("", contents, now, null),
            Arguments.of(" ", contents, now, null),
            Arguments.of(title, null, now, null),
            Arguments.of(title, "", now, null),
            Arguments.of(title, " ", now, null),
            Arguments.of(title, " ", now, null),
            Arguments.of(title, contents, null, null),
            Arguments.of(title, contents, now, now.minusHours(1L))
        );
    }

    @Test
    public void ????????????_??????() throws Exception {

        doReturn(Optional.of(Notice.builder().id(id).build())).when(noticeService).getNoticeById(id);
        
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url + "/" + id)
                .content("")
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void ????????????_??????() throws Exception {

        doReturn(Optional.empty()).when(noticeService).getNoticeById(id);
        
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url + "/" + id)
                .content("")
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void ????????????_??????() throws Exception {

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .content("")
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void ????????????() throws Exception {

        String modifiedWriter = "??????" + writer;

        NoticeDto updateDto = NoticeDto.builder()
                .title("??????" + title)
                .contents("??????" + contents)
                .periodStart(now.plusDays(1L))
                .periodEnd(now.plusDays(2L))
                .build();

        doReturn(Notice.builder()
                .id(id)
                .title(updateDto.getTitle())
                .contents(updateDto.getContents())
                .periodStart(updateDto.getPeriodStart())
                .periodEnd(updateDto.getPeriodEnd())
                .modifiedWriter(modifiedWriter)
                .modifiedDate(LocalDateTime.now())
                .build()
            ).when(noticeService).updateNotice(eq(id), any(NoticeDto.class));

        MockMultipartFile mockfile1 = new MockMultipartFile("file", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("noticeDto", "", "application/json", gson.toJson(updateDto).getBytes());

        ResultActions resultActions = mockMvc.perform(
            multipart(url + "/" + id)
                .file(mockfile1)
                .file(jsonFile)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .with(new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setMethod("PUT");
                        return request;
                    }
                })
        );
        
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void ????????????_?????????_ID() throws Exception {

        String modifiedWriter = "??????" + writer;

        NoticeDto updateDto = NoticeDto.builder()
                .title("??????" + title)
                .contents("??????" + contents)
                .periodStart(now.plusDays(1L))
                .periodEnd(now.plusDays(2L))
                .build();

        doThrow(NoticeException.class).when(noticeService).updateNotice(eq(id), any(NoticeDto.class));

        MockMultipartFile mockfile1 = new MockMultipartFile("file", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("noticeDto", "", "application/json", gson.toJson(updateDto).getBytes());

        ResultActions resultActions = mockMvc.perform(
            multipart(url + "/" + id)
                .file(mockfile1)
                .file(jsonFile)
                .header(NoticeConstants.WRITER_HEADER, writer)
                .with(new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setMethod("PUT");
                        return request;
                    }
                })
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidNoticeAddParameter")
    public void ????????????_?????????(String title, String contents, LocalDateTime periodStart, LocalDateTime periodEnd) throws Exception {

        String modifiedWriter = "??????" + writer;
        
        NoticeDto updateDto = NoticeDto.builder()
            .title(title)
            .contents(contents)
            .periodStart(periodStart)
            .periodEnd(periodEnd)
            .build();

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.put(url + "/" + id)
                .header(NoticeConstants.WRITER_HEADER, modifiedWriter)
                .content(gson.toJson(updateDto))
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void ????????????() throws Exception {

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url + "/" + id));

        resultActions.andExpect(status().isNoContent());
    }
}
