package com.kshrd.habittracker.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadFile(MultipartFile file) throws IOException;
    Resource getFilebyFileName(String fileName);
}
