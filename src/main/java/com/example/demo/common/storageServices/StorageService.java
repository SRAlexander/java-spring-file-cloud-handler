package com.example.demo.common.storageServices;

public interface StorageService {
    void UploadFile(String filename, String fileAsString) throws StorageException;
    String DownloadFile(String filename) throws StorageException;
    void DeleteFile(String filename) throws StorageException;
}
