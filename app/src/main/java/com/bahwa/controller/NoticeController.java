package com.bahwa.controller;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.service.NoticeService;

import javax.validation.Valid;

import com.bahwa.constants.NoticeConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/api/v1/notices")
    public ResponseEntity<Notice> addNotice(
            @RequestHeader(NoticeConstants.WRITER_HEADER) String writer,
            @RequestBody @Valid NoticeDto dto) {

        if (dto.getPeriodEnd() != null && 0 < dto.getPeriodStart().compareTo(dto.getPeriodEnd())) {
            throw new NoticeException(NoticeErrorResult.시작일이_종료일보다_빠름);
        }

        Notice savedNotice = noticeService.addNotice(dto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }
}
