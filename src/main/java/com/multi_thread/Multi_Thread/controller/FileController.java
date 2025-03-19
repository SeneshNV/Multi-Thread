package com.multi_thread.Multi_Thread.controller;

import com.multi_thread.Multi_Thread.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@CrossOrigin
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public String getHome(){
        return "Home (File)";
    }

    @PostMapping("/upload")
    public String postFile(@RequestParam("file") MultipartFile file){
        return fileService.uploadFile(file);
    }


//    @GetMapping("/record")
//    public List<String> recodeUpload(){
//        return fileService.UploadRecordData();
//    }
}