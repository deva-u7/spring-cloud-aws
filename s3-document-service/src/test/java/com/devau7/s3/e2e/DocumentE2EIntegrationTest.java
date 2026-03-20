package com.devau7.s3.e2e;

import com.devau7.s3.controller.DocumentController;
import com.devau7.s3.dto.DocumentDTO;
import com.devau7.s3.entity.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import static com.devau7.s3.e2e.utils.DocumentUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentE2EIntegrationTest extends IntegrationBaseTest {

    @Autowired
    private DocumentController documentController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        documentRepository.deleteAll();
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    @DisplayName("Should upload file to LocalStack S3 bucket")
    void saveDocument() throws Exception {
        DocumentDTO request = getRequest();
        String fileName = "File.txt";
        MockMultipartFile multipartFile = getMockMultipartFile(fileName);

        String responseString = mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/document")
                        .file(multipartFile)
                        .param("userName", request.getUserName())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        DocumentDTO actualResponse = objectMapper.readValue(responseString, DocumentDTO.class);
        Assertions.assertEquals(getExpectedResponse(), actualResponse);

        Document document = documentRepository.findById(actualResponse.getId()).orElseThrow();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(document.getS3Key())
                .build();

        ResponseInputStream<GetObjectResponse> s3Response = amazonS3.getObject(getObjectRequest);
        Assertions.assertArrayEquals(multipartFile.getBytes(), s3Response.readAllBytes(),
                "File content in S3 does not match uploaded content");
    }

    @Test
    @DisplayName("Should fetch uploaded file from LocalStack S3 bucket")
    void fetchDocument() throws Exception {
        MockMultipartFile multipartFile = getMockMultipartFile("File.txt");

        String responseString = mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/document")
                        .file(multipartFile)
                        .param("userName", getRequest().getUserName())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andReturn().getResponse().getContentAsString();

        DocumentDTO uploaded = objectMapper.readValue(responseString, DocumentDTO.class);

        byte[] fetched = mockMvc.perform(get("/api/v1/document/{id}", uploaded.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        Assertions.assertArrayEquals(multipartFile.getBytes(), fetched,
                "Fetched file content does not match uploaded content");
    }
}
