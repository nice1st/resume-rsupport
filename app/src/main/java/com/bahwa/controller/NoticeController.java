package com.bahwa.controller;

import com.bahwa.dto.NoticeDto;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.service.AttachmentsService;
import com.bahwa.service.NoticeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@ExecuteTimeCheck
public class NoticeController {

    private final NoticeService noticeService;

    private final AttachmentsService attachmentsService;

    private final Executor executor;
    
    @PostMapping("/api/v1/notices")
    public ResponseEntity<Notice> addNotice(
            @RequestHeader(NoticeConstants.WRITER_HEADER) String writer,
            @RequestPart(value = "noticeDto") @Valid NoticeDto noticeDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) throws Exception {

        if (isPeriodValid(noticeDto.getPeriodStart(), noticeDto.getPeriodEnd())) {
            throw new NoticeException(NoticeErrorResult.시작일이_종료일보다_빠름);
        }

        noticeDto.setWriter(writer);
        Notice savedNotice = noticeService.addNotice(noticeDto);

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            attachmentsService.addAttachments(savedNotice.getId(), multipartFileList);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }

    private boolean isPeriodValid(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (endDateTime != null && 0 < startDateTime.compareTo(endDateTime));
    }

    @GetMapping("/api/v1/notices/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable("id") long id) {

        Optional<Notice> result = noticeService.getNoticeById(id);

        if (result.isPresent()) {
            // 조회수 증가 // 비동기
            CompletableFuture.runAsync(() -> noticeService.incrementViews(result.get()), executor);
            
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
        @RequestPart(value = "noticeDto") @Valid NoticeDto noticeDto,
        @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList) throws Exception {
    
        if (isPeriodValid(noticeDto.getPeriodStart(), noticeDto.getPeriodEnd())) {
            throw new NoticeException(NoticeErrorResult.시작일이_종료일보다_빠름);
        }

        noticeDto.setModifiedWriter(modifiedWriter);
        Notice updatedNotice = noticeService.updateNotice(id, noticeDto);

        if (multipartFileList != null && !multipartFileList.isEmpty()) {
            attachmentsService.updateAttachments(updatedNotice.getId(), multipartFileList);
        }
        
        return ResponseEntity.ok(updatedNotice);
    }

    @DeleteMapping("/api/v1/notices/{id}")
    public ResponseEntity<Object> removeNotice(@PathVariable("id") long id) {

        noticeService.removeNoticeById(id);
        attachmentsService.removeAttachmentsByNoticeId(id);

        return ResponseEntity.noContent().build();
    }
}
