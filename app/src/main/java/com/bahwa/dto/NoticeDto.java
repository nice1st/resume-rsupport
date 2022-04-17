package com.bahwa.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotBlank
    @Size(min=4, max=100)
    private String title;
    @NotBlank
    @Size(min=2, max=10000)
    private String contents;
    @Size(min=1, max=50)
    private String writer;
    @Size(min=1, max=50)
    private String modifiedWriter;
    @NotNull
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
