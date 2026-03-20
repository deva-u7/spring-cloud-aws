package com.devau7.s3.dto;

import com.devau7.s3.FileUploadStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentDTO {
    private Long id;
    private String userName;
    private FileUploadStatus uploadStatus;
}
