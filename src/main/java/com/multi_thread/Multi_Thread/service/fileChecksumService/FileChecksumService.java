package com.multi_thread.Multi_Thread.service.fileChecksumService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileChecksumService {

    public String getFileChecksum(MultipartFile file){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

                InputStream inputStream = file.getInputStream();

                byte[] buffer = new byte[8192];
                int byteRead;

                while ((byteRead = inputStream.read(buffer)) != -1){
                    digest.update(buffer, 0, byteRead);
                }

            byte[] hashBytes = digest.digest();

            //convert byte to hex
            StringBuilder hexString = new StringBuilder();

            for(byte b : hashBytes){
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Error computing checksum",e);
        }
    }
}
