package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class DriveFirstActivity extends AppCompatActivity {
    public static final int REQUEST_CODE=200;
    private MinioClient minioClient;
    private List<Bucket> bucketList;
    private ListView listView;
    private Intent intent;
    public static final String BUCKET_NAME = "bucketName";
    private int poz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_first);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        listView=findViewById(R.id.listViewBuckets);

        try {
            bucketGetter();
        }
        catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e){
            e.printStackTrace();
        }
        if (!bucketList.isEmpty()){
            loadList();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                poz=position;
                intent=new Intent(getApplicationContext(), DriveSecondActivity.class);
                Bucket bk=bucketList.get(position);
                intent.putExtra(BUCKET_NAME, bk.name());
                startActivity(intent);

            }
        });

    }

    public void loadList(){
        CustomAdapter adapter=new CustomAdapter(getApplicationContext(), R.layout.bucket_list_view, bucketList, getLayoutInflater()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Bucket b1=bucketList.get(position);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    public void bucketGetter()throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException{
        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint("10.0.2.2", 9000, false)
                            .credentials("myminio", "myminio123")
                            .build();

            bucketList = minioClient.listBuckets();

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}