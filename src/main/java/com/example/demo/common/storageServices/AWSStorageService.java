package com.example.demo.common.storageServices;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Service
public class AWSStorageService implements StorageService {

    // Configuration singleton parameters
    private String _bucketName;
    private String _accountKey;
    private String _accountSecret;
    private String _region;

    private AmazonS3 _s3Client;

    public AWSStorageService(){
        _accountKey = System.getenv("AWS_ACCOUNT_KEY");
        _accountSecret = System.getenv("AWS_ACCOUNT_SECRET");
        _bucketName = System.getenv("AWS_BUCKET_NAME");
        _region = System.getenv("AWS_REGION");

        AWSCredentials credentials = new BasicAWSCredentials(_accountKey, _accountSecret);
        _s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(_region)
                .build();
    }

    public void UploadFile(String filename, String fileAsString) throws StorageException {

        Integer retryAttempts = 0;
        Boolean successful = Boolean.FALSE;
        while (retryAttempts < 3) {
            try {
                com.amazonaws.services.s3.model.ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(fileAsString.length());
                InputStream inputStream = new ByteArrayInputStream(fileAsString.getBytes(StandardCharsets.UTF_8));

                _s3Client.putObject(_bucketName, filename, inputStream, metadata );

                successful = ValidateUploadedFile(filename, fileAsString);
                if (successful) {
                    retryAttempts = 3;
                } else {
                    DeleteFile(filename);
                    retryAttempts++;
                }
            }
            catch (Exception e) {
                throw new StorageException("Error occurred uploading to S3 Bucket", e);
            }
        }
    }

    public String DownloadFile(String filename) throws StorageException {
        try {
            S3ObjectInputStream stream = DownloadFileToStream(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String text = "";
            String temp = "";

            while ((temp = bufferedReader.readLine()) != null) {
                text = text + temp;
            }

            bufferedReader.close();
            stream.close();
            return text;
        }
        catch (IOException e) {
            throw new StorageException("Error occurred downloading from S3 Bucket", e);
        }
    }

    public void DeleteFile(String filename){
        _s3Client.deleteObject(_bucketName,filename);
    }

    private S3ObjectInputStream DownloadFileToStream(String filename) throws StorageException {
        try {
            S3Object s3Object = _s3Client.getObject(_bucketName, filename);
            return s3Object.getObjectContent();
        }
        catch (Exception exception) {
            throw new StorageException("Error occurred downloading from S3 Bucket", exception);
        }
    }

    private Boolean ValidateUploadedFile(String filename, String fileAsString) throws StorageException {
        return fileAsString.equals(DownloadFile(filename));
    }

}
