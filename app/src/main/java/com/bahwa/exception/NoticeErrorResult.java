package com.bahwa.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorResult {
    
    시작일이_종료일보다_빠름(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 빠름"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
