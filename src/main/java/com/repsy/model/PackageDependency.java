package com.repsy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("package") // JSON'da "package" anahtarı için
    @Column(name = "package_name") // DB'de column adı farklı olsun
    private String pkg;

    private String version;

    @ManyToOne
    @JoinColumn(name = "metadata_id")
    private PackageMetadata metadata;
}
