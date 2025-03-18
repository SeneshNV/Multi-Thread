package com.multi_thread.Multi_Thread.scheduler;

import com.multi_thread.Multi_Thread.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FixCronScheduler {

    private final FileService fileService;

    public FixCronScheduler(FileService fileService) {
        this.fileService = fileService;
    }

    @Scheduled(cron ="*/15 * * * * ?")
    public void task(){
        try {
            String s = fileService.UploadRecordData();
            System.out.println(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Fixed rate task : ");
    }
}
