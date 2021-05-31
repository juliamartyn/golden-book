package com.juliamartyn.goldenbook;

import com.juliamartyn.goldenbook.services.AmazonS3ClientService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class AmazonS3ClientServiceMock implements AmazonS3ClientService {

    @Override
    public String uploadFileToS3(MultipartFile file, String bucketName, String folderName){
        return null;
    }

    @Override
    public byte[] downloadFile(String fileReference, String bucketName, String folderName){
        return new byte[0];
    }
}
