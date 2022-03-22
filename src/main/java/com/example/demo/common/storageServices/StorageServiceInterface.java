package com.example.demo.common.storageServices;

interface StorageServiceInterface {
    void UploadFile(String filename, String fileAsString) throws StorageException;
    String DownloadFile(String filename) throws StorageException;
    void DeleteFile(String filename) throws StorageException;
}
