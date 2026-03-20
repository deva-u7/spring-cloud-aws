package com.devau7.s3.entity;

import com.devau7.s3.FileUploadStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private FileUploadStatus uploadStatus = FileUploadStatus.PENDING;
    private String fileURL;
    private String s3Key;
}
