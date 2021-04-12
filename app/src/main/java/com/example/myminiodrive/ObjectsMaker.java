package com.example.myminiodrive;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

public class ObjectsMaker extends AsyncTask<String, Void, MinioClient> {

    MinioClient minioClient = null;
    public List<Objects> objectRetrived = new ArrayList<>();

    @Override
    protected MinioClient doInBackground(String... strings) {
        uploader(strings[0], strings[1], strings[2]);
        return minioClient;
    }

    public void uploader(String bucketName, String fileName, String path){
        Iterable<Result<Item>> results;
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName).object(fileName).filename(path).build());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
