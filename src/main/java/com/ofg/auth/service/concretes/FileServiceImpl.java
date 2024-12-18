package com.ofg.auth.service.concretes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

import com.ofg.auth.config.AppProperties;
import com.ofg.auth.exception.file.FileServiceException;
import com.ofg.auth.service.abstracts.FileService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {
    private final AppProperties appProperties;
    private final Tika tika;

    private static final String DEFAULT_EXTENSION = ".png";

    @Autowired
    public FileServiceImpl(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.tika = new Tika();
    }

    @Override
    public String saveFile(String imageType, MultipartFile file) {
        String filename = generateFilename(file.getOriginalFilename());
        Path path = getImagePath(imageType, filename);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new FileServiceException("save");
        }
    }

    private String generateFilename(String originalFilename) {
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : DEFAULT_EXTENSION;
        return UUID.randomUUID() + extension;
    }

    @Override
    public String saveBase64StringAsFile(String imageType, String image) {
        String filename = UUID.randomUUID().toString();
        Path path = getImagePath(imageType, filename);

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            outputStream.write(decodedImage(image));
            return filename;
        } catch (IOException e) {
            throw new FileServiceException("save64");
        }
    }

    @Override
    public String detectType(String value) {
        return tika.detect(decodedImage(value));
    }

    private byte[] decodedImage(String encodedImage) {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    @Override
    public void deleteImage(String imageType, String image) {
        if (image == null) {
            throw new FileServiceException("image null");
        }
        if ("default.png".equals(image)) {
            return;
        }

        Path path = getImagePath(imageType, image);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileServiceException("delete");
        }
    }

    private Path getImagePath(String imageType, String filename) {
        String folder = switch (imageType) {
            case "profile" -> appProperties.getStorage().getProfile();
            default -> throw new IllegalArgumentException("Unknown image type: " + imageType);
        };
        return Paths.get(appProperties.getStorage().getRoot(), folder, filename);
    }
}
