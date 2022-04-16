package com.bahwa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.bahwa.entity.Notice;

@DataJpaTest
public class NoticeRepositoryTest {
    
    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void 등록() { 
        
        Notice notice = Notice.builder()
            .title("title1")
            .contents("contents test")
            .writer("writer1")
            .periodStart(LocalDateTime.now())
            .periodEnd(LocalDateTime.now().plusHours(1))
            .build();

        Notice result = noticeRepository.save(notice);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getIsDeleted()).isNotNull();
        assertThat(result.getIsDeleted()).isEqualTo(false);
        assertThat(result.getViews().equals(0L));
        
        assertThat(result.getCreatedDate().isBefore(LocalDateTime.now()));
    }

    @Test
    public void 조회_단일() {
        
        Random random = new Random();

        Optional<Notice> result = noticeRepository.findById(random.nextLong());
    }

    @Test
    public void 조회_전체() {

        List<Notice> result = noticeRepository.findAll();

        assertThat(result.size()).isGreaterThan(-1);
    }

    @Test
    public void 삭제() {

        Notice notice = Notice.builder()
            .title("title1")
            .contents("contents test")
            .writer("writer1")
            .periodStart(LocalDateTime.now())
            .periodEnd(LocalDateTime.now().plusHours(1))
            .build();

        Notice result = noticeRepository.save(notice);

        noticeRepository.deleteById(result.getId());
    }
}
