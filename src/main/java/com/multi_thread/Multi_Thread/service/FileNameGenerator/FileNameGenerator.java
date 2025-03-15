package com.multi_thread.Multi_Thread.service.FileNameGenerator;

import java.time.LocalDate;

public class FileNameGenerator {

    public static String generateFileName(int fileNumber, String originalFilename){
        //get extension
        String extension = "";

        //get extension start index
        int lastDotIndex = originalFilename.lastIndexOf(".");

        //get extension
        if(lastDotIndex != -1){
            extension = originalFilename.substring(lastDotIndex);
        }

        LocalDate date = LocalDate.now();

        // unique file name
        return "file_" + fileNumber + "_" + date + extension;

    }
}
