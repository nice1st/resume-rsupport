package com.bahwa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.bahwa.dto.FileDto;
import com.bahwa.entity.Attachments;
import com.bahwa.entity.Notice;
import com.bahwa.exception.NoticeErrorResult;
import com.bahwa.exception.NoticeException;
import com.bahwa.repository.AttachmentsRepository;
import com.bahwa.repository.NoticeRepository;
import com.bahwa.util.FileUtilities;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentsService {
    
    private final AttachmentsRepository attachmentsRepository;

    private final NoticeService noticeService;

    private final FileUtilities fileUtilities;

    public List<Attachments> addAttachments(Long noticeId, List<MultipartFile> fileList) throws Exception {

        Notice notice = noticeService.getNoticeById(noticeId).orElseThrow(() -> new NoticeException(NoticeErrorResult.파일_업로드_실패));

        List<Attachments> attachmentsList = uploadAttachments(notice, fileList);
        attachmentsList.forEach(attachments -> attachmentsRepository.save(attachments));

        return attachmentsList;
    }

    public List<Attachments> updateAttachments(Long noticeId, List<MultipartFile> fileList) throws Exception {
        
        Notice notice = noticeService.getNoticeById(noticeId).orElseThrow(() -> new NoticeException(NoticeErrorResult.파일_업로드_실패));

        this.removeAttachmentsByNoticeId(noticeId);

        List<Attachments> attachmentsList = uploadAttachments(notice, fileList);
        attachmentsList.forEach(attachments -> attachmentsRepository.save(attachments));

        return attachmentsList;
    }

    public Optional<Attachments> getAttachmentsById(Long id) {
        
        return attachmentsRepository.findById(id);
    }

    public List<Attachments> getAttachmentsByNoticeId(Long noticeId) {

        return attachmentsRepository.findAllByNoticeId(noticeId);
    }

    public void removeAttachmentsByNoticeId(Long noticeId) {

        List<Attachments> attachmentsList =  this.getAttachmentsByNoticeId(noticeId);
        attachmentsList.forEach(attachments -> {
            fileUtilities.removeFile(attachments.getFilePath());
        });

        attachmentsRepository.deleteByNoticeId(noticeId);
    }

    private List<Attachments> uploadAttachments(Notice notice, List<MultipartFile> fileList) throws Exception {
        
        List<Attachments> result = new ArrayList<>();

        fileList.stream().forEach(file -> {
            FileDto fileDto;
            try {
                fileDto = fileUtilities.uploadFile(file);
            } catch (Exception e) {
                throw new NoticeException(NoticeErrorResult.파일_업로드_실패);
            }

            Attachments attachments = Attachments.builder()
                    .notice(notice)
                    .originFileName(fileDto.getOriginFileName())
                    .fileName(fileDto.getFileName())
                    .filePath(fileDto.getFilePath())
                    .fileSize(fileDto.getFileSize())
                    .build();

            result.add(attachments);
        });

        return result;
    }
}
