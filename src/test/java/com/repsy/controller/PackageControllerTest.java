package com.repsy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUploadPackageAndSaveMetadata() throws Exception {
        // Test verisi: package.rep (boş içerik) ve meta.json
        InputStream repStream = new ClassPathResource("testfiles/package.rep").getInputStream();
        InputStream metaStream = new ClassPathResource("testfiles/meta.json").getInputStream();

        MockMultipartFile packageFile = new MockMultipartFile(
                "package", "package.rep", "application/octet-stream", repStream);

        MockMultipartFile metaFile = new MockMultipartFile(
                "meta", "meta.json", "application/json", metaStream);

        mockMvc.perform(multipart("/mypackage/1.0.0")
                        .file(packageFile)
                        .file(metaFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
}
