package com.example.myminiodrive;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;

public class BucketMaker extends AsyncTask <String, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<MyBuckets> bucketsRetrived;

    @Override
    protected MinioClient doInBackground(String... strings) {

        create(strings[0]);
        return minioClient;
    }

    public void create(String edittext){
        List<Bucket> bucketList;
        bucketsRetrived =  new ArrayList<>();
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(edittext)
                            .build());

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
