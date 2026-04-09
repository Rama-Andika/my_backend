package com.oxysystem.general.util;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileUtils {
    public void saveFile(byte[] fileBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBytes);
        }
    }
}
