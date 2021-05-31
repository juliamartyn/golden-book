package com.juliamartyn.goldenbook.services;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
    String uploadFileToS3(MultipartFile file, String bucketName, String folderName);
    byte[] downloadFile(String fileReference, String bucketName, String folderName);
}
