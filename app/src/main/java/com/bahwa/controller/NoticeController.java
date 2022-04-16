package com.bahwa.controller;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.service.NoticeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.bahwa.aop.ExecuteTimeCheck;
import com.bahwa.constants.NoticeConstants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@ExecuteTimeCheck
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/api/v1/notices")
    public ResponseEntity<Notice> addNotice(
            @RequestHeader(NoticeConstants.WRITER_HEADER) String writer,
            @RequestBody @Valid NoticeDto dto) {

        if (isPeriodValid(dto.getPeriodStart(), dto.getPeriodEnd())) {
            throw new NoticeException(NoticeErrorResult.시작일이_종료일보다_빠름);
        }

        dto.setWriter(writer);
        Notice savedNotice = noticeService.addNotice(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }

    private boolean isPeriodValid(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (endDateTime != null && 0 < startDateTime.compareTo(endDateTime));
    }

    @GetMapping("/api/v1/notices/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable("id") long id) {

        Optional<Notice> result = noticeService.getNoticeById(id);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/api/v1/notices")
    public ResponseEntity<List<Notice>> getNoticeById() {

        List<Notice> result = noticeService.getNoticeAll();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/v1/notices/{id}")
    public ResponseEntity<Notice> updateNotice(
        @RequestHeader(NoticeConstants.WRITER_HEADER) String modifiedWriter,
        @PathVariable("id") long id,
        @RequestBody @Valid NoticeDto dto) {
        
        if (isPeriodValid(dto.getPeriodStart(), dto.getPeriodEnd())) {
            throw new NoticeException(NoticeErrorResult.시작일이_종료일보다_빠름);
        }

        dto.setId(id);
        dto.setModifiedWriter(modifiedWriter);
        Notice updatedNotice = noticeService.updateNotice(dto);
        
        return ResponseEntity.ok(updatedNotice);
    }

    @DeleteMapping("/api/v1/notices/{id}")
    public ResponseEntity<Object> removeNotice(@PathVariable("id") long id) {

        noticeService.removeNoticeById(id);

        return ResponseEntity.noContent().build();
    }
}
