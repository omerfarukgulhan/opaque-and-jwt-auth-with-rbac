package com.ofg.auth.service.abstracts;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(String path, MultipartFile file);

    String saveBase64StringAsFile(String path, String image);

    String detectType(String value);

    void deleteImage(String path, String image);
}
