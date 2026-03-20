package com.devau7.s3.service;

import com.devau7.s3.FileUploadStatus;
import com.devau7.s3.dto.DocumentDTO;
import com.devau7.s3.entity.Document;
import com.devau7.s3.mapper.ModelMapper;
import com.devau7.s3.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper modelMapper;
    private final AWSS3FileService s3FileService;

    public DocumentDTO saveDocuments(DocumentDTO documentDTO, MultipartFile file) {
        log.info("Saving document: {}", documentDTO);
        Document document = documentRepository.save(modelMapper.toEntity(documentDTO));
        try {
            if (file != null) {
                log.info("Uploading file: {}", file.getOriginalFilename());
                Pair<String, String> response = s3FileService.fileUpload(file);
                document.setUploadStatus(FileUploadStatus.COMPLETED);
                document.setFileURL(response.getFirst());
                document.setS3Key(response.getSecond());
                documentRepository.save(document);
            }
            return modelMapper.toDTO(document);
        } catch (Exception ex) {
            document.setUploadStatus(FileUploadStatus.FAILED);
            documentRepository.save(document);
            log.error("Error saving document", ex);
            throw new RuntimeException("Exception while uploading document", ex);
        }
    }

    public Pair<HttpHeaders, byte[]> fetchDocument(Long documentId) throws IOException {
        log.info("Fetching document id: {}", documentId);
        Document document = documentRepository.findById(documentId).orElseThrow();

        if (!document.getUploadStatus().equals(FileUploadStatus.COMPLETED)) {
            throw new RuntimeException("Document not uploaded for id: " + documentId);
        }

        ResponseInputStream<GetObjectResponse> s3Response = s3FileService.fetchDocumentByKey(document.getS3Key());
        GetObjectResponse metadata = s3Response.response();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(metadata.contentType()));
        headers.setContentLength(metadata.contentLength());
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(metadata.metadata().getOrDefault("file-name", "file"))
                        .build());

        return Pair.of(headers, s3Response.readAllBytes());
    }
}
