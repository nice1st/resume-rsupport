package com.bahwa.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeException extends RuntimeException {
    
    private final NoticeErrorResult noticeErrorResult;
}
