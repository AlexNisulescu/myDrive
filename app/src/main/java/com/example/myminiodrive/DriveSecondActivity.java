package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;


public class DriveSecondActivity extends AppCompatActivity {
    private MinioClient minioClient;
    private List<Objects> objectList= new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_second);

        listView=findViewById(R.id.listViewObjects);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Intent intent=getIntent();

        String name=(String) intent.getSerializableExtra(DriveFirstActivity.BUCKET_NAME);

        try {
            objectsGetter(name);
        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
        }
        if (!objectList.isEmpty()){
            loadList();
        }
    }

    public void loadList(){
        ObjectCustomAdapter adapter=new ObjectCustomAdapter(getApplicationContext(), R.layout.object_list_view, objectList, getLayoutInflater()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                View view=super.getView(position ,convertView, parent);
                Objects obiect=objectList.get(position);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    public void objectsGetter(String bname) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results;
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bname).recursive(true).build());

        for(Result<Item> res : results){
            Item item=res.get();
            Objects obj=new Objects(item.objectName(), item.size(), item.lastModified());
            objectList.add(obj);
        }
    }
}