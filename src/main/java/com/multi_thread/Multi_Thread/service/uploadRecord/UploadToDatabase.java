package com.multi_thread.Multi_Thread.service.uploadRecord;

import com.multi_thread.Multi_Thread.entity.FileRecordDetailsEntity;
import com.multi_thread.Multi_Thread.entity.FileDetailsEntity;
import com.multi_thread.Multi_Thread.repository.FileRecordDetailsRepository;
import com.multi_thread.Multi_Thread.repository.FileDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UploadToDatabase {

    private final FileRecordDetailsRepository fileRecordDetailsRepository;
    private final FileDetailsRepository fileDetailsRepository;

    public UploadToDatabase(FileRecordDetailsRepository fileRecordDetailsRepository,
                            FileDetailsRepository fileDetailsRepository) {
        this.fileRecordDetailsRepository = fileRecordDetailsRepository;
        this.fileDetailsRepository = fileDetailsRepository;
    }

    @Transactional
    public void addToRecordDatabase(StringBuilder sentences, Long fileId) {
        String content = sentences.toString();


        Long lineNumber = fileRecordDetailsRepository.findMaxLineNumber(fileId)
                .orElse(0L);


        lineNumber += 1L;

        System.out.println("Content: " + content);
        System.out.println("File ID: " + fileId);
        System.out.println("New Line Number: " + lineNumber);


        FileDetailsEntity fileDetailsEntity = fileDetailsRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("FileDetailsEntity not found for ID: " + fileId));


        FileRecordDetailsEntity newRecord = new FileRecordDetailsEntity();
        newRecord.setFileDetailsEntity(fileDetailsEntity);
        newRecord.setLineNumber(lineNumber);
        newRecord.setContent(content);


        fileRecordDetailsRepository.save(newRecord);

        System.out.println("New record saved successfully!");

        updateFileStatus(fileId);
    }

    private void updateFileStatus(Long fileId) {
        FileDetailsEntity fileDetailsEntity = fileDetailsRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("FileDetailsEntity not found for ID: " + fileId));


        fileDetailsEntity.setStatus("Done");
        fileDetailsRepository.save(fileDetailsEntity);

        System.out.println("File status updated to 'Done' for file ID: " + fileId);
    }
}