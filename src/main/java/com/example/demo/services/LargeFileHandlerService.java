package com.example.demo.services;

import com.example.demo.common.storageServices.AzureStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LargeFileHandlerService {

    private final AzureStorageService _azureStorageService;

    public LargeFileHandlerService(AzureStorageService azureStorageService) {
        _azureStorageService = azureStorageService;
    }

    public String GetLargeFile() throws IOException {
        _azureStorageService.AddFileStringAndValidate("saTestFile" + LocalDateTime.now().toString() + ".txt", "Hello Main Test");
        return "Hell World Service";
    }
}
