package com.example.demo.common.storageServices;

public class StorageException extends RuntimeException {
    public StorageException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
