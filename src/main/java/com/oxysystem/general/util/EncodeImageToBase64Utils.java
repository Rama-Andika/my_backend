package com.oxysystem.general.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class EncodeImageToBase64Utils {
    public String encodeImageToBase64(String imagePath) throws IOException {
        // Read the image as bytes
        Path path = Paths.get(imagePath);
        byte[] imageBytes = Files.readAllBytes(path);
        // Encode to Base64
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
