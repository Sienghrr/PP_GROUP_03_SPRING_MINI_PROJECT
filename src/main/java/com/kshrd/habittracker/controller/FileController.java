package com.kshrd.habittracker.controller;

import com.kshrd.habittracker.dto.response.ApiResponse;
import com.kshrd.habittracker.dto.response.FileResponse;
import com.kshrd.habittracker.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URLConnection;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = fileService.uploadFile(file);
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/files/preview-file/").path(fileName).toString();
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFileName(fileName);
        fileResponse.setFileUrl(fileUrl);
        fileResponse.setFileType(file.getContentType());
        fileResponse.setFileSize(file.getSize());

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(fileResponse, "File upload succesfully to RustFS", HttpStatus.CREATED)


        );
    }

    @GetMapping("/preview-file/{fileName}")
    public ResponseEntity<?> viewFileByFileName(@PathVariable String fileName){
    Resource resource = fileService.getFilebyFileName(fileName);
    String contentType = URLConnection.guessContentTypeFromName(fileName);

    return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);

    }


}
