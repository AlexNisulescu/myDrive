package com.example.myminiodrive;

import android.os.AsyncTask;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.minio.MinioClient;
import io.minio.messages.Bucket;

public class BucketsGetter extends AsyncTask<URL, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<MyBuckets> bucketsRetrived;

    @Override
    protected MinioClient doInBackground(URL... urls) {
        retriever(urls[0]);

        return minioClient;
    }

    public void retriever(URL url){
        List<Bucket> bucketList;
        String endpoint = url.toString();
        bucketsRetrived =  new ArrayList<>();
        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint(endpoint, 9000, false)
                            .credentials("myminio", "myminio123")
                            .build();

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
