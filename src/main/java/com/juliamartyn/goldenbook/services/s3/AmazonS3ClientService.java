package com.juliamartyn.goldenbook.services.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class AmazonS3ClientService {
    @Value("${S3_ACCESSKEY}")
    private String accessKey;

    @Value("${S3_SECRETKEY}")
    private String secretKey;

    @Value("${S3_FOLDER_NAME}")
    @Getter
    private String folderName;

    @Value("${S3_BUCKET_NAME}")
    @Getter
    private String bucketName;

    private AmazonS3 s3client;

    @PostConstruct
    public void connectClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public String uploadFileToS3(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            s3client.putObject(bucketName,  folderName + "/" + fileReference, file.getInputStream(), new ObjectMetadata());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileReference;
    }

    public byte[] downloadFile(String fileReference, String bucketName, String folderName) {
        byte[] bytes = new byte[0];
        try {
            bytes = s3client.getObject(bucketName, folderName + "/" + fileReference).getObjectContent().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
