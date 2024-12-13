package com.example.sm.minh.eshop.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUltil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, String oldFileName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not save image file: " + fileName, ex);
        }

        // Delete old image file
        if (oldFileName != null && !oldFileName.isEmpty()) {
            Path oldFilePath = uploadPath.resolve(oldFileName);
            Files.deleteIfExists(oldFilePath);
        }
    }
}
