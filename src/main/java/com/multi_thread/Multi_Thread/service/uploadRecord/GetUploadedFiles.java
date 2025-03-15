package com.multi_thread.Multi_Thread.service.uploadRecord;

import com.multi_thread.Multi_Thread.entity.FileDetailsEntity;
import com.multi_thread.Multi_Thread.repository.FileDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUploadedFiles {

    private final FileDetailsRepository fileDetailsRepository;

    public GetUploadedFiles(FileDetailsRepository fileDetailsRepository) {
        this.fileDetailsRepository = fileDetailsRepository;
    }

    public List<String> displayFilePaths() {
        List<FileDetailsEntity> uploadedFiles = fileDetailsRepository.findAllUpdatedFileDetailsEntity();

        if (uploadedFiles.isEmpty()) {
            System.out.println("No uploaded files found.");
            return List.of(); // Return an empty list if no files are found
        }

        // Collect file paths into a List<String>
        List<String> filePaths = uploadedFiles.stream()
                .map(FileDetailsEntity::getFilePath)
                .collect(Collectors.toList());

        System.out.println("Uploaded Files: " + filePaths);
        return filePaths;
    }
}
