package com.repsy.config;

import com.repsy.storage.FileSystemStorageService;
import com.repsy.storage.MinioStorageService;
import com.repsy.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${storage.strategy}")
    private String strategy;

    @Bean
    public StorageService storageService() throws Exception {
        switch (strategy.toLowerCase()) {
            case "file-system":
                return new FileSystemStorageService();
            case "object-storage":
                return new MinioStorageService();
            default:
                throw new IllegalArgumentException("Unsupported strategy: " + strategy);
        }
    }
}
