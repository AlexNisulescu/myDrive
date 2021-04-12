package com.example.myminiodrive;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;

public class BucketRemover extends AsyncTask<String, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<MyBuckets> bucketsRetrived;

    @Override
    protected MinioClient doInBackground(String... strings) {
        remover(strings[0]);
        return minioClient;
    }

    public void remover(String bucketname){
        List<Bucket> bucketList;
        bucketsRetrived =  new ArrayList<>();

        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint("10.0.2.2", 9000, false)
                            .credentials("myminio", "myminio123")
                            .build();

            try {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketname).build());
            } catch (ErrorResponseException |
                    InsufficientDataException |
                    InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                e.printStackTrace();
            }
            bucketList = minioClient.listBuckets();
            for(Bucket b : bucketList){
                MyBuckets mb=new MyBuckets(b.name(), b.creationDate());
                bucketsRetrived.add(mb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
