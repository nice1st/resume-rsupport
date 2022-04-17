package com.bahwa.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileDto {
    
    @NotBlank
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;
}
