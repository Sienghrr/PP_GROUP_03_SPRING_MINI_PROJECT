package com.kshrd.habittracker.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class FileResponse {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
