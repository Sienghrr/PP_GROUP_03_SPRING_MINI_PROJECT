package com.kshrd.habittracker.service.impl;

import com.kshrd.habittracker.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final String bucketName = "files";

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        createBucketIfNotExists();

        String originalFileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newFileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return newFileName;
    }


    private void createBucketIfNotExists() {
        try {
            HeadBucketRequest request = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(request);
        } catch (S3Exception e) {
            CreateBucketRequest request = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(request);
        }
    }


    @Override
    public Resource getFilebyFileName(String fileName) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(request);

        return new InputStreamResource(inputStream);
    }

}
