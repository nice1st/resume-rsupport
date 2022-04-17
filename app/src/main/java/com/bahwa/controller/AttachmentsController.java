package com.bahwa.controller;

import java.io.File;
import java.io.FileInputStream;

import com.bahwa.aop.ExecuteTimeCheck;
import com.bahwa.entity.Attachments;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.service.AttachmentsService;
import com.bahwa.util.FileUtilities;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@ExecuteTimeCheck
public class AttachmentsController {
    
    private final AttachmentsService attachmentsService;

    private final FileUtilities fileUtilities;

    @GetMapping("/api/v1/attachments/download/{id}")
    public ResponseEntity<InputStreamResource> downloadAttachments(@PathVariable("id") long id) {

        Attachments attachments = attachmentsService.getAttachmentsById(id).orElseThrow(() -> new NoticeException(NoticeErrorResult.파일_다운로드_실패));

        try {
            
            File targetFile = fileUtilities.getFile(attachments.getFilePath());
        
            InputStreamResource resource = new InputStreamResource(new FileInputStream(targetFile));

            return ResponseEntity.ok() 
                // .headers(header) 
                .contentLength(targetFile.length()) 
                .contentType(MediaType.parseMediaType("application/octet-stream")) 
                .body(resource);
        } catch (Exception e) {
            new NoticeException(NoticeErrorResult.파일_다운로드_실패);
        }

        return null; 
    }

}
