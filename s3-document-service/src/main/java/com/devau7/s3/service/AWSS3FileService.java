package com.devau7.s3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AWSS3FileService {

    private final S3Client amazonS3;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    public static final String S3_BASE_URL = "https://s3.amazonaws.com/";

    public Pair<String, String> fileUpload(MultipartFile multipartFile) {
        log.info("Starting file upload: {}", multipartFile.getOriginalFilename());
        try {
            String fileName = multipartFile.getOriginalFilename();
            String s3Key = String.format("/object/%s", fileName);
            File file = convertMultiPartFileToFile(multipartFile);

            uploadResourceToS3Bucket(bucketName, file, s3Key);
            file.delete();

            return Pair.of(getResourceUrl(bucketName, s3Key), s3Key);
        } catch (AwsServiceException e) {
            log.error("AWS Service error uploading: {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("AWS Service error during file upload", e);
        } catch (IOException e) {
            log.error("IO error uploading: {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("File processing error during upload", e);
        }
    }

    public ResponseInputStream<GetObjectResponse> fetchDocumentByKey(String key) {
        log.debug("Fetching from bucket: {}, key: {}", bucketName, key);
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            return amazonS3.getObject(request);
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("File not found in bucket with key: " + key, e);
        } catch (SdkClientException e) {
            throw new RuntimeException("Error accessing S3 bucket", e);
        }
    }

    private String getResourceUrl(String bucketName, String path) {
        return S3_BASE_URL + bucketName + path;
    }

    private void uploadResourceToS3Bucket(String bucketName, File file, String s3Key) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        amazonS3.putObject(putRequest, RequestBody.fromFile(file));
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File("temp_" + multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
        return file;
    }
}
