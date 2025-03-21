package com.multi_thread.Multi_Thread.service;

import com.multi_thread.Multi_Thread.entity.FileDetailsEntity;
import com.multi_thread.Multi_Thread.repository.FileDetailsRepository;
import com.multi_thread.Multi_Thread.repository.FileRecordDetailsRepository;
import com.multi_thread.Multi_Thread.service.FileNameGenerator.FileNameGenerator;
import com.multi_thread.Multi_Thread.service.fileChecksumService.FileChecksumService;
import com.multi_thread.Multi_Thread.service.uploadRecord.GetUploadedFiles;
import com.multi_thread.Multi_Thread.thread.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class FileService {

    ThreadPool threadPool = new ThreadPool(3, 10);

    private final FileDetailsRepository fileDetailsRepository;
    private final FileRecordDetailsRepository fileRecordDetailsRepository;

    String fileUploadStatus;

    public FileService(FileDetailsRepository fileDetailsRepository, FileRecordDetailsRepository fileRecordDetailsRepository) {
        this.fileDetailsRepository = fileDetailsRepository;
        this.fileRecordDetailsRepository = fileRecordDetailsRepository;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // original file name
            String originalFilename = file.getOriginalFilename();

            //call to check sum class
            // 1. create checksum value
            FileChecksumService fileChecksumService = new FileChecksumService();
            String checksumHexString = fileChecksumService.getFileChecksum(file);
            System.out.println("File Checksum: " + checksumHexString);

            log.info("==================== before checking uniquness ===============");

            // 2. check the new fill is unique
            if (fileDetailsRepository.existsByChecksum(checksumHexString)) {
                fileUploadStatus = "File is already exist";
                System.out.println(fileUploadStatus);
                return fileUploadStatus;
            } else {
                fileUploadStatus = "File is unique";
                System.out.println(fileUploadStatus);
            }

            //generate unique file name
            int lastFileIDNumber = fileDetailsRepository.findMaxId().orElse(0);
            int newFileNumber = lastFileIDNumber + 1;

            String uniqueFileName = FileNameGenerator.generateFileName(newFileNumber, originalFilename);

            //new file path
            String uploadDir = System.getProperty("user.dir") + File.separator + "Uploads";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = uploadDir + File.separator + uniqueFileName;

            System.out.println("Uploaded file name: " + uniqueFileName);

            FileOutputStream fout = new FileOutputStream(filePath);
            fout.write(file.getBytes());

            fout.close();

            System.out.println("File Uploaded Successfully" + "\n | Checksum: " + checksumHexString);

            // 3. insert data to database
            FileDetailsEntity fileDetailsEntity = new FileDetailsEntity();
            fileDetailsEntity.setFileCode(uniqueFileName);
            fileDetailsEntity.setFilePath(filePath);
            fileDetailsEntity.setUploadAt(new Date());
            fileDetailsEntity.setChecksum(checksumHexString);
            fileDetailsEntity.setStatus("uploaded");

            fileDetailsRepository.save(fileDetailsEntity);

            fileUploadStatus = "File Uploaded Successfully" + "\n | Checksum: " + checksumHexString;

        }

        // Catch block to handle exceptions
        catch (Exception e) {
            e.printStackTrace();
            fileUploadStatus =  "Error in uploading file: " + e;
        }
        return fileUploadStatus;
    }

    public String UploadRecordData() {

        GetUploadedFiles getUploadedFiles = new GetUploadedFiles(fileDetailsRepository, fileRecordDetailsRepository);

        List<String> filePaths = getUploadedFiles.displayFilePaths();
        System.out.println(filePaths);

        if (filePaths.isEmpty()) {
            return "No uploaded files found.";
        }

        for (String filePath : filePaths) {

            try{
                threadPool.execute( () -> {
                    List<String> fileDataRecords = getUploadedFiles.readFile(filePath);
                    System.out.println("Processed file: " + filePath);

                });

            } catch (Exception e) {
                System.err.println("Error processing file: " + filePath);
                throw new RuntimeException(e);
            }

        }
        threadPool.waitUntilAllTasksFinished();
//        threadPool.stop();

        return "Current recorded are insert to Record Data Table";
    }
}