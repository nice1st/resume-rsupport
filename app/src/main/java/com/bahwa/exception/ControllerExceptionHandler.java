package com.bahwa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoticeException.class)
    protected ResponseEntity<Object> noticeException(NoticeException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getNoticeErrorResult());
    }
}
