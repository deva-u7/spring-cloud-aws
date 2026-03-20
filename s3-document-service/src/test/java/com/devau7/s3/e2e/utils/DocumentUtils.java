package com.devau7.s3.e2e.utils;

import com.devau7.s3.FileUploadStatus;
import com.devau7.s3.dto.DocumentDTO;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class DocumentUtils {

    public static DocumentDTO getRequest() {
        return DocumentDTO.builder().userName("TestUser").build();
    }

    public static DocumentDTO getExpectedResponse() {
        return DocumentDTO.builder()
                .id(1L)
                .userName("TestUser")
                .uploadStatus(FileUploadStatus.COMPLETED)
                .build();
    }

    public static MockMultipartFile getMockMultipartFile(String fileName) {
        return new MockMultipartFile(
                "file",
                fileName,
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes());
    }
}
