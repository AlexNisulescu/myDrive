package com.example.myminiodrive;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

public class ObjectsSearch extends AsyncTask<String, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<Objects> objectRetrived = new ArrayList<>();

    @Override
    protected MinioClient doInBackground(String... strings) {

        Search(strings[0], strings[1]);
        return minioClient;
    }

    public void Search(String bucketName, String objectName){
        Iterable<Result<Item>> results;
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).prefix((objectName)).build());
        for(Result<Item> res : results){
            try {
                Item item = res.get();
                Objects obj=new Objects(item.objectName(), item.size(), item.lastModified());
                objectRetrived.add(obj);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
