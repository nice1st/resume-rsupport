package com.bahwa.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NoticeDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    private String writer;
    private String modifiedWriter;
    @NotNull
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long views;
    private Boolean isDeleted;
}
