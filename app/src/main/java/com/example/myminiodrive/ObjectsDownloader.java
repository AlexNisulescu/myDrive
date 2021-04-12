package com.example.myminiodrive;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class ObjectsDownloader extends AsyncTask<String, Void, MinioClient> {

    MinioClient minioClient = null;
    private InputStream in;
    private ByteArrayOutputStream out;
    private byte[] response;
    @Override
    protected MinioClient doInBackground(String... strings) {
        download(strings[0], strings[1]);
        return minioClient;
    }

    public void download(String bucketName, String objectName){
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        try {
            in = new BufferedInputStream(minioClient
                    .getObject(GetObjectArgs.builder()
                            .bucket(bucketName).object(objectName).build()));
            out = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[2048];
                int n = 0;
                while (-1 != (n = in.read(buffer))) {
                    out.write(buffer, 0, n);
                }
                out.close();
                in.close();
                response = out.toByteArray();
                FileOutputStream fos = new FileOutputStream("/sdcard/Download/"+objectName);
                fos.write(response);
                fos.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
