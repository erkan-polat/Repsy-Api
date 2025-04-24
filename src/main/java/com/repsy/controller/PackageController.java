package com.repsy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.model.PackageDependency;
import com.repsy.model.PackageMetadata;
import com.repsy.repository.PackageMetadataRepository;
import com.repsy.storage.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class PackageController {

    private final StorageService storageService;
    private final PackageMetadataRepository metadataRepository;

    public PackageController(StorageService storageService, PackageMetadataRepository metadataRepository) {
        this.storageService = storageService;
        this.metadataRepository = metadataRepository;
    }

    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile
    ) throws IOException {

        // Uzantı kontrolü
        if (!FilenameUtils.getExtension(packageFile.getOriginalFilename()).equals("rep")
                || !FilenameUtils.getExtension(metaFile.getOriginalFilename()).equals("json")) {
            return ResponseEntity.badRequest().body("Invalid file types. Only .rep and .json are allowed.");
        }

        // Dosyaları kaydet
        storageService.save(packageName + "/" + version + "/package.rep", packageFile.getInputStream());
        storageService.save(packageName + "/" + version + "/meta.json", metaFile.getInputStream());

        // meta.json'u Java nesnesine parse et
        ObjectMapper mapper = new ObjectMapper();
        PackageMetadata metadata = mapper.readValue(metaFile.getInputStream(), PackageMetadata.class);

        // Bağımlılıklarla ilişkilendir
        if (metadata.getDependencies() != null) {
            for (PackageDependency dep : metadata.getDependencies()) {
                dep.setMetadata(metadata);
            }
        }

        // Veritabanına kaydet
        metadataRepository.save(metadata);

        return ResponseEntity.ok("Package uploaded and metadata saved: " + packageName + " " + version);
    }
    @GetMapping("/packages")
    public ResponseEntity<?> getAllPackages() {
        return ResponseEntity.ok(metadataRepository.findAll());
    }
    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<?> downloadPackageFile(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName
    ) {
        try {
            String path = packageName + "/" + version + "/" + fileName;
            byte[] data = storageService.load(path).readAllBytes();
    
            // Normalize filename
            String safeFileName = fileName.trim().toLowerCase();
    
            System.out.println("DEBUG >> fileName: [" + fileName + "]");
            System.out.println("DEBUG >> safeFileName: [" + safeFileName + "]");
            System.out.println("DEBUG >> endsWith('.json'): " + safeFileName.endsWith(".json"));
    
            if (safeFileName.endsWith(".json")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(data);
            }
    
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
}
