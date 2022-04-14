package com.bahwa.service;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.repository.NoticeRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
    
    private final NoticeRepository noticeRepository;

    public Notice addNotice(NoticeDto dto) {

        Notice notice = Notice.builder()
            .writer(dto.getWriter())
            .title(dto.getTitle())
            .contents(dto.getContents())
            .periodStart(dto.getPeriodStart())
            .build();

        return noticeRepository.save(notice);
    }
}
