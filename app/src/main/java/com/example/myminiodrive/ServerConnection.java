package com.example.myminiodrive;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ServerConnection implements Runnable {
    @Override
    public void run() {
        try {
            FileUploader();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void FileUploader() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("10.0.2.2", 9000, false)
                            .credentials("myminio", "myminio123")
                            .build();

            /*boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("test123").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("test123").build());
            } else {
                System.out.println("Bucket 'test123' already exists.");
            }

             */
            List<Bucket> bucketList = minioClient.listBuckets();
            for (Bucket bucket : bucketList) {
                System.out.println(bucket.creationDate() + ", " + bucket.name());
            }

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

    }
}
