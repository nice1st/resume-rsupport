package com.bahwa.repository;

import com.bahwa.configuration.QueryDslConfiguration;
import com.bahwa.entity.Attachments;
import com.bahwa.entity.Notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import(QueryDslConfiguration.class)
public class AttachmentsRepositoryTest {

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private final String originFileName = "file1";
    private final String fileName = "f_file1";
    private final String filePath = "/dir";
    private final Long fileSize = 10000L;

    private Notice notice;

    @BeforeEach
    public void init() {
        
        notice = noticeRepository.save(
            Notice.builder()
                .title("title")
                .contents("contents")
                .writer("writer")
                .periodStart(LocalDateTime.now())
                .build());
    }

    @Test
    public void 등록() {
        
        Attachments attachments = Attachments.builder()
            .notice(notice)
            .originFileName(originFileName)
            .fileName(fileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .build();

        Attachments result = attachmentsRepository.save(attachments);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void 조회() {
        Attachments attachments1 = Attachments.builder()
            .notice(notice)
            .originFileName(originFileName)
            .fileName(fileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .build();

        Attachments attachments2 = Attachments.builder()
            .notice(notice)
            .originFileName(originFileName + " a")
            .fileName(fileName + " a")
            .filePath(filePath + " a")
            .fileSize(fileSize)
            .build();

        attachmentsRepository.save(attachments1);
        attachmentsRepository.save(attachments2);

        List<Attachments> result = attachmentsRepository.findAllByNoticeId(notice.getId());

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 삭제() {
        Attachments attachments1 = Attachments.builder()
            .notice(notice)
            .originFileName(originFileName)
            .fileName(fileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .build();

        Attachments attachments2 = Attachments.builder()
            .notice(notice)
            .originFileName(originFileName + " a")
            .fileName(fileName + " a")
            .filePath(filePath + " a")
            .fileSize(fileSize)
            .build();

        attachmentsRepository.save(attachments1);
        attachmentsRepository.save(attachments2);

        attachmentsRepository.deleteByNoticeId(notice.getId());

        List<Attachments> result = attachmentsRepository.findAllByNoticeId(notice.getId());

        assertThat(result.size()).isEqualTo(0);
    }
}
