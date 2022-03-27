package com.example.demo.services;

import com.example.demo.common.storageServices.StorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LargeFileHandlerService {

    private final StorageService _storageService;

    public LargeFileHandlerService(StorageService storageService) {
        _storageService = storageService;
    }

    public String AddFile(String filename) throws IOException {
        _storageService.UploadFile("saTestFile" + LocalDateTime.now().toString() + ".txt", "Hello Main Test");
        return "File Added";
    }

    public String GetFile(String filename) throws IOException {
        _storageService.DownloadFile(filename);
        return "File Retrieved";
    }

    public String DeleteFile(String filename) {
        _storageService.DeleteFile(filename);
        return "File Deleted";
    }
}
