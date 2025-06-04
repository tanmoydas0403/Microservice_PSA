package com.propertyservice.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;


@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // Flag to enable or disable S3 uploads
    @Value("${aws.s3.enabled:false}")
    private boolean s3Enabled;

    public List<String> uploadFiles(MultipartFile[] files) {
        //when S3 is not enabled, return empty list or mock URLs
        if (!s3Enabled) {
            // Return empty or mock URLs in dev mode
            return List.of();
        }
        //actual S3 upload logic
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
                String url = amazonS3.getUrl(bucketName, fileName).toString();
                urls.add(url);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file to S3", e);
            }
        }
        return urls;
    }
}