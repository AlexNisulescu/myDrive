package com.example.myminiodrive;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

public class ObjectsGetter extends AsyncTask <String, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<Objects> objectRetrived = new ArrayList<>();
    @Override
    protected MinioClient doInBackground(String... strings) {

        getter(strings[0]);
        return minioClient;
    }

    public void getter(String bucketName){
        Iterable<Result<Item>> results;
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).build());

        for(Result<Item> res : results){
            Item item= null;
            try {
                item = res.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Objects obj=new Objects(item.objectName(), item.size(), item.lastModified());
            objectRetrived.add(obj);
        }
    }
}
