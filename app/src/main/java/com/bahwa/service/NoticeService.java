package com.bahwa.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.repository.NoticeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    
    private final NoticeRepository noticeRepository;

    private final Executor executor;

    public Notice addNotice(NoticeDto dto) {

        Notice notice = Notice.builder()
            .writer(dto.getWriter())
            .title(dto.getTitle())
            .contents(dto.getContents())
            .periodStart(dto.getPeriodStart())
            .periodEnd(dto.getPeriodEnd())
            .build();

        return noticeRepository.save(notice);
    }

    public Optional<Notice> getNoticeById(Long id) {

        Optional<Notice> result = noticeRepository.findById(id);
        
        if (result.isPresent()) {
            // 조회수 증가 // 비동기
            // this.incrementViews(result.get());
            CompletableFuture.runAsync(() -> incrementViews(result.get()), executor);
        }

        return result;
    }

    public List<Notice> getNoticeAll() {
        
        return noticeRepository.findAllByBetweenPeriodDateTime();
    }

    public Notice updateNotice(Long id, NoticeDto dto) {

        Notice notice = this.getNoticeById(id).orElseThrow(() -> new NoticeException(NoticeErrorResult.수정_할_공지사항이_없음));

        notice.setTitle(dto.getTitle());
        notice.setContents(dto.getContents());
        notice.setPeriodStart(dto.getPeriodStart());
        notice.setPeriodEnd(dto.getPeriodEnd());
        notice.setModifiedWriter(dto.getModifiedWriter());

        return noticeRepository.save(notice);
    }

    public void removeNoticeById(Long id) {

        Notice notice = this.getNoticeById(id).orElseThrow(() -> new NoticeException(NoticeErrorResult.삭제_할_공지사항이_없음));

        noticeRepository.delete(notice);
    }

    public void incrementViews(Notice notice) {

        notice.setViews(notice.getViews() + 1);

        noticeRepository.save(notice);
    }
}
