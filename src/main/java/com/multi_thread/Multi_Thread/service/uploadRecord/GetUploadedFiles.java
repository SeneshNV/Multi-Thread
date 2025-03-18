package com.multi_thread.Multi_Thread.service.uploadRecord;

import com.multi_thread.Multi_Thread.entity.FileDetailsEntity;
import com.multi_thread.Multi_Thread.repository.FileDetailsRepository;
import com.multi_thread.Multi_Thread.repository.FileRecordDetailsRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetUploadedFiles {

    private final FileDetailsRepository fileDetailsRepository;
    private final FileRecordDetailsRepository fileRecordDetailsRepository;

    public GetUploadedFiles(FileDetailsRepository fileDetailsRepository, FileRecordDetailsRepository fileRecordDetailsRepository) {
        this.fileDetailsRepository = fileDetailsRepository;
        this.fileRecordDetailsRepository = fileRecordDetailsRepository;
    }

    public List<String> displayFilePaths() {
        List<FileDetailsEntity> uploadedFiles = fileDetailsRepository.findAllUpdatedFileDetailsEntity();

        if (uploadedFiles.isEmpty()) {
            System.out.println("No uploaded files found.");
            return List.of(); // Return an empty list if no files are found
        }

        List<String> filePaths = uploadedFiles.stream()
                .map(FileDetailsEntity::getFilePath)
                .collect(Collectors.toList());

        System.out.println("Uploaded Files: " + filePaths);
        return filePaths;
    }

    //read the file line by line
    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        UploadToDatabase uploadToDatabase = new UploadToDatabase(fileRecordDetailsRepository, fileDetailsRepository);

        Optional<FileDetailsEntity> fileDetailsEntityOptional = fileDetailsRepository.findByFilePath(filePath);


            FileDetailsEntity fileDetailsEntity = fileDetailsEntityOptional.get();
        Long fileId = fileDetailsEntity.getId();

            System.out.println("File ID: " + fileId);


        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            StringBuilder sentences = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {



                    String[] numbers = line.split("\\.");
                    String line_no_s = numbers[0];
                    int number_length = line_no_s.length();

                    try {
                        Integer.parseInt(line_no_s);
//                        System.out.println("int");
//                        System.out.println("Line No. : " + line_no_s);

                        String Updateline = line.substring(number_length +1).trim();

                        sentences.append(Updateline).append(" ");

                    } catch (NumberFormatException e) {
//                        System.out.println("not int");

                        sentences.append(line).append(" ");
                    }

                } else {
                    if (sentences.length() > 0) {
                        lines.add(sentences.toString().trim());

                        uploadToDatabase.addToRecordDatabase(sentences, fileId);

                        System.out.println(sentences);
                        sentences.setLength(0);
                    }
                }
            }

            if (sentences.length() > 0) {
                lines.add(sentences.toString().trim());
                uploadToDatabase.addToRecordDatabase(sentences, fileId);
                System.out.println(sentences);
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
        return lines;
    }

}



