package com.example.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static String getCustomFileName(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //remove spaces and make lowercase
        filename = filename.toLowerCase().replaceAll(" ", "-");
        // Splitting by .
        String[] splitted = filename.split("\\.");

        // TODO add exception to handle when the file name is not correct
        return filename;
    }

    public static void saveFile(MultipartFile file) throws IOException {
        String filename = getCustomFileName(file);
        Path path = Paths.get("C:\\Users\\user\\Desktop\\leanring\\spring", filename);
        Files.copy(file.getInputStream(), path);
    }
}
