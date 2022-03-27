package com.example.demo.common.storageServices;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
public class LocalStorageService implements StorageService {

    // Configuration singleton parameters
    private final Map<String, byte[]> storage;

    public LocalStorageService(){
        storage = new HashMap<>();
    }

    public void UploadFile(String filename, String fileAsString) throws StorageException {

        Integer retryAttempts = 0;
        Boolean successful = Boolean.FALSE;
        while (retryAttempts < 3) {
            try {
                storage.put(filename, fileAsString.getBytes(StandardCharsets.UTF_8));
                successful = ValidateUploadedFile(filename, fileAsString);
                if (successful) {
                    retryAttempts = 3;
                } else {
                    DeleteFile(filename);
                    retryAttempts++;
                }
            }
            catch (Exception e) {
                throw new StorageException("Error occurred uploading to Local Storage", e);
            }
        }
    }

    public String DownloadFile(String filename) throws StorageException {
        try {
            InputStream inputStream = DownloadFileToStream(filename);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            throw new StorageException("Error occurred downloading from Local Storage", e);
        }
    }

    public void DeleteFile(String filename){
        try {
        storage.remove(filename);
        }
        catch (Exception e) {
            throw new StorageException("Error occurred deleting from Local Storage", e);
        }
    }

    private InputStream DownloadFileToStream(String filename) throws StorageException {
        try {
            byte[] objectBytes = storage.get(filename);
            return new ByteArrayInputStream(objectBytes);
        }
        catch (Exception exception) {
            throw new StorageException("Error occurred downloading from S3 Bucket", exception);
        }
    }

    private Boolean ValidateUploadedFile(String filename, String fileAsString) throws StorageException {
        return fileAsString.equals(DownloadFile(filename));
    }

}
