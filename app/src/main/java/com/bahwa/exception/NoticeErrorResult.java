package com.bahwa.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorResult {
    
    시작일이_종료일보다_빠름(HttpStatus.BAD_REQUEST, "시작일이_종료일보다_빠름"),
    수정_할_공지사항이_없음(HttpStatus.BAD_REQUEST, "수정_할_공지사항이_없음"),
    삭제_할_공지사항이_없음(HttpStatus.BAD_REQUEST, "삭제_할_공지사항이_없음"),
    파일_업로드_실패(HttpStatus.BAD_REQUEST, "파일_업로드_실패"),
    파일_다운로드_실패(HttpStatus.NO_CONTENT, "파일_다운로드_실패"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
