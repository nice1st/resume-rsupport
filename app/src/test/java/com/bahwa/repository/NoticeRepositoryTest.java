package com.bahwa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import com.bahwa.entity.Notice;

@DataJpaTest
public class NoticeRepositoryTest {
    
    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void 등록() { 
        
        final Notice notice = Notice.builder()
            .title("title1")
            .contents("contents test")
            .writer("writer1")
            .periodStart(LocalDateTime.now())
            .periodEnd(LocalDateTime.now().plusHours(1))
            .build();

        final Notice result = noticeRepository.save(notice);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getIsDeleted()).isNotNull();
        assertThat(result.getIsDeleted()).isEqualTo(false);
        assertThat(result.getViews().equals(0L));
        
        assertThat(result.getCreatedDate().isBefore(LocalDateTime.now()));
    }
}
