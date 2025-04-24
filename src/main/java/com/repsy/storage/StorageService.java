package com.repsy.storage;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    void save(String path, InputStream content) throws IOException;
    InputStream load(String path) throws IOException;
}
