package com.devau7.s3.controller;

import com.devau7.s3.dto.DocumentDTO;
import com.devau7.s3.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DocumentDTO> saveDocument(
            @ModelAttribute DocumentDTO documentDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(documentService.saveDocuments(documentDTO, file));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<byte[]> getDocumentById(@PathVariable Long documentId) throws IOException {
        Pair<HttpHeaders, byte[]> response = documentService.fetchDocument(documentId);
        return ResponseEntity.ok()
                .headers(response.getFirst())
                .body(response.getSecond());
    }
}
