package com.example.demo.common.storageServices;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;


@Service
public class AzureStorageService implements StorageService {

    // Configuration singleton parameters
    private String _accountName;
    private String _accountKey;
    private String _mainContainer;

    // Consistent objects
    private BlobServiceClient _activeBlobServiceClient;
    private BlobContainerClient _activeBlobContainerClient;

    public AzureStorageService(){
        _accountName = System.getenv("AZURE_ACCOUNT_NAME");
        _accountKey = System.getenv("AZURE_ACCOUNT_KEY");
        _mainContainer = System.getenv("AZURE_MAIN_CONTAINER");
    }

    public void UploadFile(String filename, String fileAsString) throws StorageException {

        Integer retryAttempts = 0;
        Boolean successful = Boolean.FALSE;
        while (retryAttempts < 3) {
            try {
                AddFileStringToMainContainer(filename, fileAsString);
                successful = ValidateUploadedFile(filename, fileAsString);
                if (successful) {
                    retryAttempts = 3;
                } else {
                    DeleteFile(filename);
                    retryAttempts++;
                }
            }
            catch (IOException e) {
                throw new StorageException("Failed adding file to Azure Blob storage", e);
            }

        }
    }

    public String DownloadFile(String filename) throws StorageException {
        ByteArrayOutputStream stream = DownloadFileToStream(filename);
        return new String(stream.toByteArray(), StandardCharsets.UTF_8);
    }

    public void DeleteFile(String filename){
        BlobContainerClient containerClient = CreateBlobContainerClient();
        BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();
        blobClient.delete();
    }

    private StorageSharedKeyCredential CreateAzureCredentials(){
        return new StorageSharedKeyCredential(_accountName, _accountKey);
    }

    private String CreateAzureStorageEndpoint(){
        return String.format(Locale.ROOT, "https://%s.blob.core.windows.net", System.getenv("AZURE_ACCOUNT_NAME"));
    }

    private BlobServiceClient CreateBlobServiceClient(){
        return new BlobServiceClientBuilder()
                .endpoint(CreateAzureStorageEndpoint())
                .credential(CreateAzureCredentials())
                .buildClient();
    }

    private BlobContainerClient CreateBlobContainerClient() {
        BlobServiceClient blobServiceClient = CreateBlobServiceClient();
        return blobServiceClient.getBlobContainerClient(_mainContainer);
    }

    private void AddFileStringToMainContainer(String filename, String fileAsString) throws StorageException, IOException {
        BlobContainerClient containerClient = CreateBlobContainerClient();
        BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();
        InputStream dataStream = new ByteArrayInputStream(fileAsString.getBytes(StandardCharsets.UTF_8));
        blobClient.upload(dataStream, fileAsString.length());
        dataStream.close();
    }

    private ByteArrayOutputStream DownloadFileToStream(String filename) throws StorageException {
        try {
            BlobContainerClient containerClient = CreateBlobContainerClient();
            BlockBlobClient blobClient = containerClient.getBlobClient(filename).getBlockBlobClient();
            int dataSize = (int) blobClient.getProperties().getBlobSize();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);
            blobClient.downloadStream(outputStream);
            outputStream.close();
            return outputStream;
        }
        catch (IOException e) {
            throw new StorageException("Failed to download blob for Azure Blob storage", e);
        }
    }

    private Boolean ValidateUploadedFile(String filename, String fileAsString) throws StorageException {
        return fileAsString.equals(DownloadFile(filename));
    }

}
