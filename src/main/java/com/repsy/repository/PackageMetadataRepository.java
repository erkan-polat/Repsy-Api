package com.repsy.repository;

import com.repsy.model.PackageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageMetadataRepository extends JpaRepository<PackageMetadata, Long> {
}
