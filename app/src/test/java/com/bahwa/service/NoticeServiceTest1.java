package com.bahwa.service;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoticeServiceTest1 {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    private final LocalDateTime now = LocalDateTime.now();
    private final Long id = 1L;
    private final String writer = "user1";
    private final String title = "title1";
    private final String contents = "contents1234567890";

    private final String modifyWriter = "user2";

    private Notice createNotice() {
        return Notice.builder()
                .writer(writer)
                .title(title)
                .contents(contents)
                .periodStart(now)
                .build();
    }

    @Test
    public void 단일조회_incrementView() throws InterruptedException {

        noticeRepository.saveAndFlush(createNotice());

        int threadCount = 10;
        ExecutorService es = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            es.submit(new IncrementViewTask());
        }

        es.shutdown();

        es.awaitTermination(1, TimeUnit.HOURS);

        Notice notice = noticeRepository.findById(id).orElseThrow();
        assertThat(notice.getViews()).isEqualTo(threadCount);
    }

    private class IncrementViewTask implements Runnable {

        @Override
        public void run() {
            try {
                noticeService.getNoticeByIdWithIncrementView(id);
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " - " + e.getClass().getName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
