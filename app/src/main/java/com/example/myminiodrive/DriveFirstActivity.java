package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
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
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DriveFirstActivity extends AppCompatActivity {
    public static final int REQUEST_CODE=200;
    private MinioClient minioClient;
    private List<MyBuckets> bucketsList= new ArrayList<>();
    private ListView listView;
    private Intent intent;
    public static final String BUCKET_NAME = "bucketName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_first);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        listView = findViewById(R.id.listViewBuckets);
        FloatingActionButton btn=findViewById(R.id.floatingActionButton);

        try {
            bucketGetter();
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            e.printStackTrace();
        }
        if (!bucketsList.isEmpty()) {
            loadList();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), DriveSecondActivity.class);
                MyBuckets bk = bucketsList.get(position);
                intent.putExtra(BUCKET_NAME, bk.getName());
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomAdapter adapter = (CustomAdapter) listView.getAdapter();
                MyBuckets b1=bucketsList.get(position);

                AlertDialog dialog=new AlertDialog.Builder(DriveFirstActivity.this).setTitle("Confirmare Stergere")
                        .setMessage("Sigur doriti stergerea")
                        .setNegativeButton("NU", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                Toast.makeText(getApplicationContext(), "Nu am sters nimic", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).setPositiveButton("DA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {

                                try {
                                    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(b1.getName()).build());
                                } catch (ErrorResponseException |
                                        InsufficientDataException |
                                        InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                                    e.printStackTrace();
                                }
                                bucketsList.remove(b1);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(getApplicationContext(), "S-a sters : "+b1.getName(), Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).create();

                dialog.show();
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAdapter adapter = (CustomAdapter) listView.getAdapter();
                AlertDialog.Builder alert = new AlertDialog.Builder(DriveFirstActivity.this);
                final EditText edittext = new EditText(getApplicationContext());
                alert.setMessage("Enter the name of the bucket");
                alert.setView(edittext);

                alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            minioClient.makeBucket(
                                    MakeBucketArgs.builder()
                                            .bucket(edittext.getText().toString())
                                            .build());
                            Clock cl = Clock.systemUTC();
                            MyBuckets b=new MyBuckets(edittext.getText().toString(), ZonedDateTime.now(cl));
                            bucketsList.add(b);
                            adapter.notifyDataSetChanged();
                        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alert.show();
            }
        });
    }

    public void loadList(){
        CustomAdapter adapter=new CustomAdapter(getApplicationContext(), R.layout.bucket_list_view, bucketsList, getLayoutInflater()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                MyBuckets b1=bucketsList.get(position);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    public void bucketGetter()throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException{
        List<Bucket> bucketList;
        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint("10.0.2.2", 9000, false)
                            .credentials("myminio", "myminio123")
                            .build();

            bucketList = minioClient.listBuckets();
            for(Bucket b : bucketList){
               MyBuckets mb=new MyBuckets(b.name(), b.creationDate());
               bucketsList.add(mb);
            }

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}