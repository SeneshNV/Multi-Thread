package com.multi_thread.Multi_Thread.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    String fileUploadStatus;

    public String uploadFile(MultipartFile file) {
        // Extract the original file name
        String originalFilename = file.getOriginalFilename();

        // Print file name to console
        System.out.println("Uploaded file name: " + originalFilename);

        System.out.println(file.getSize());

        String filePath = System.getProperty("user.dir") + "/Uploads" + File.separator + file.getOriginalFilename();

        try {

            // Creating an object of FileOutputStream class
            FileOutputStream fout = new FileOutputStream(filePath);
            fout.write(file.getBytes());

            // Closing the connection
            fout.close();
            fileUploadStatus = "File Uploaded Successfully";

        }

        // Catch block to handle exceptions
        catch (Exception e) {
            e.printStackTrace();
            fileUploadStatus =  "Error in uploading file: " + e;
        }
        return fileUploadStatus;
    }
}
