package com.example.demo.services;

import com.example.demo.common.storageServices.AWSStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LargeFileHandlerService {

    private final AWSStorageService _azureStorageService;

    public LargeFileHandlerService(AWSStorageService azureStorageService) {
        _azureStorageService = azureStorageService;
    }

    public String AddFile(String filename) throws IOException {
        _azureStorageService.UploadFile("saTestFile" + LocalDateTime.now().toString() + ".txt", "Hello Main Test");
        return "File Added";
    }

    public String GetFile(String filename) throws IOException {
        _azureStorageService.DownloadFile(filename);
        return "File Retrieved";
    }

    public String DeleteFile(String filename) {
        _azureStorageService.DeleteFile(filename);
        return "File Deleted";
    }
}
