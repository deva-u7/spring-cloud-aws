package com.devau7.s3.mapper;

import com.devau7.s3.dto.DocumentDTO;
import com.devau7.s3.entity.Document;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModelMapper {
    DocumentDTO toDTO(Document document);
    Document toEntity(DocumentDTO documentDTO);
}
