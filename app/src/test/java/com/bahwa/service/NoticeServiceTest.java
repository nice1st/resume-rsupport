package com.bahwa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import javax.validation.ConstraintViolationException;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.repository.NoticeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {
    
    @InjectMocks
    private NoticeService target;
    
    @Mock
    private NoticeRepository noticeRepository;

    private final LocalDateTime now = LocalDateTime.now();
    private final Long id = 1L;
    private final String writer = "user1";
    private final String title = "title1";
    private final String contents = "contents1234567890";

    private final String modifyWriter = "user2";

    @Test
    public void 등록성공() {
        
        doReturn(Notice.builder().id(id).title(title).contents(contents).periodStart(now).build()).when(noticeRepository).save(any(Notice.class));
        
        NoticeDto dto = NoticeDto.builder()
            .writer(writer)
            .title(title)
            .contents(contents)
            .periodStart(now)
            .build();

        Notice result = target.addNotice(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContents()).isEqualTo(contents);
    }
}
