package com.bahwa.service;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    
    private final NoticeRepository noticeRepository;

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

    // @Cacheable(value = "notice", key = "#id") // views 계속 업데이트
    public Optional<Notice> getNoticeById(Long id) {

        return noticeRepository.findById(id);
    }

    @Cacheable("notice")
    public List<Notice> getNoticeAll() {
        
        return noticeRepository.findAllByBetweenPeriodDateTime();
    }

    @CachePut(value = "notice", key = "#dto.id")
    public Notice updateNotice(Long id, NoticeDto dto) {

        Notice notice = this.getNoticeById(id).orElseThrow(() -> new NoticeException(NoticeErrorResult.수정_할_공지사항이_없음));

        notice.setTitle(dto.getTitle());
        notice.setContents(dto.getContents());
        notice.setPeriodStart(dto.getPeriodStart());
        notice.setPeriodEnd(dto.getPeriodEnd());
        notice.setModifiedWriter(dto.getModifiedWriter());

        return noticeRepository.save(notice);
    }

    @CacheEvict(value = "notice", key = "#id")
    public void removeNoticeById(Long id) {

        Notice notice = this.getNoticeById(id).orElseThrow(() -> new NoticeException(NoticeErrorResult.삭제_할_공지사항이_없음));

        noticeRepository.delete(notice);
    }

    public void incrementViews(Notice notice) {

        notice.setViews(notice.getViews() + 1);

        noticeRepository.save(notice);
    }

    public Optional<Notice> getNoticeByIdWithIncrementView(Long id) {

        Optional<Notice> result;
        try {
            result = noticeRepository.findWithPessimisticLockById(id);
        } catch (Exception e) {
            // H2 에러 처리 못함. https://blog.mimacom.com/handling-pessimistic-locking-jpa-oracle-mysql-postgresql-derbi-h2/
            throw e;
        }

        if (result.isEmpty()) {
            return result;
        }

        this.incrementViews(result.get());

        return result;
    }
}
