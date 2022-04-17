package com.bahwa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {
    
    @InjectMocks
    private NoticeService target;
    
    @Mock
    private NoticeRepository noticeRepository;

    @Spy
    private Executor executor;

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

    @Test
    public void 조회성공_단일() {
        
        doReturn(Optional.of(Notice.builder().id(id).build())).when(noticeRepository).findById(id);

        Optional<Notice> result = target.getNoticeById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    public void 조회성공_전체() {
        
        doReturn(Arrays.asList(
            Notice.builder().build(),
            Notice.builder().build()
        )).when(noticeRepository).findAll();

        List<Notice> result = target.getNoticeAll();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 수정성공() {

        String modifiedWriter = "수정" + writer;

        NoticeDto updateDto = NoticeDto.builder()
                .title("수정" + title)
                .contents("수정" + contents)
                .periodStart(now.plusDays(1L))
                .periodEnd(now.plusDays(2L))
                .build();

        doReturn(Optional.of(Notice.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .build()
            )).when(noticeRepository).findById(id);

        doReturn(Notice.builder()
                .id(id)
                .title(updateDto.getTitle())
                .contents(updateDto.getContents())
                .periodStart(updateDto.getPeriodStart())
                .periodEnd(updateDto.getPeriodEnd())
                .modifiedWriter(modifiedWriter)
                .modifiedDate(LocalDateTime.now())
                .build()
            ).when(noticeRepository).save(any(Notice.class));

        Notice result = target.updateNotice(id, updateDto);
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(result.getContents()).isEqualTo(updateDto.getContents());
        assertThat(result.getModifiedWriter()).isEqualTo(modifiedWriter);
        assertThat(result.getModifiedDate()).isNotNull();
    }

    @Test
    public void 수정실패_잘못된_ID() {
        
        NoticeDto updateDto = NoticeDto.builder()
                .title("수정" + title)
                .contents("수정" + contents)
                .periodStart(now.plusDays(1L))
                .periodEnd(now.plusDays(2L))
                .build();

        doReturn(Optional.empty()).when(noticeRepository).findById(id);

        NoticeException result = assertThrows(NoticeException.class, () -> target.updateNotice(id, updateDto));

        assertThat(result.getNoticeErrorResult()).isEqualTo(NoticeErrorResult.수정_할_공지사항이_없음);
    }

    @Test
    public void 삭제성공() {
        
        doReturn(Optional.of(Notice.builder().id(id).build())).when(noticeRepository).findById(id);

        target.removeNoticeById(id);
    }
    
    @Test
    public void 삭제실패_잘못된_ID() {
        
        doReturn(Optional.empty()).when(noticeRepository).findById(id);

        NoticeException result = assertThrows(NoticeException.class, () -> target.removeNoticeById(id));

        assertThat(result.getNoticeErrorResult()).isEqualTo(NoticeErrorResult.삭제_할_공지사항이_없음);
    }

    @Test
    public void 카운트_증가() {
        
        target.incrementViews(Notice.builder().id(id).views(0L).build());

        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

}
