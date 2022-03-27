package com.example.demo.common.storageServices;

import lombok.Setter;

@Setter
public class StorageServiceFactory {
    private StorageService _storageService;

//    private StorageServiceConfiguration configuration;

//    @Override
    public StorageService getObject() {
        if (_storageService == null) {
            switch (StorageServiceOptionsEnum.enumOf(System.getenv("STORAGE_INSTANCE"))) {
                case S3:
                    _storageService = new AWSStorageService();
                    break;
                case AZURE:
                    _storageService = new AzureStorageService();
                    break;
                default:
                    _storageService = new LocalStorageService();
            }
        }
        return _storageService;
    }

//    @Override
    public Class<?> getObjectType() {
        return StorageService.class;
    }
}
