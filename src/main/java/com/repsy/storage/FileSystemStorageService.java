package com.repsy.storage;

import java.io.*;
import java.nio.file.*;

public class FileSystemStorageService implements StorageService {

    private final String basePath = "uploads";

    public FileSystemStorageService() throws IOException {
        Path uploadPath = Paths.get(basePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @Override
    public void save(String path, InputStream content) throws IOException {
        Path fullPath = Paths.get(basePath, path);
        Files.createDirectories(fullPath.getParent());
        Files.copy(content, fullPath);
    }

    @Override
    public InputStream load(String path) throws IOException {
        Path fullPath = Paths.get(basePath, path);
        return Files.newInputStream(fullPath);
    }
}
