package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DriveFirstActivity extends AppCompatActivity {
    private List<MyBuckets> bucketsList= new ArrayList<>();
    private ListView listView;
    private Intent intent;
    public static final String BUCKET_NAME = "bucketName";
    public static ProgressDialog pgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_first);

        listView = findViewById(R.id.listViewBuckets);
        FloatingActionButton btn=findViewById(R.id.floatingActionButton);

        BucketsGetter b = new BucketsGetter(){
            @Override
            protected void onPostExecute(MinioClient minioClient) {
                bucketsList = bucketsRetrived;
                loadList();
            }
        };try {
            b.execute(new URL("http://10.0.2.2"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
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

                AlertDialog dialog=new AlertDialog.Builder(DriveFirstActivity.this).setTitle("Delete Bucket")
                        .setMessage("Are you sure you want to delete this bucket?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                Toast.makeText(getApplicationContext(), "Nothing was deleted", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                BucketRemover b = new BucketRemover(){
                                    @Override
                                    protected void onPostExecute(MinioClient minioClient) {
                                        bucketsList = bucketsRetrived;
                                        loadList();
                                    }
                                };b.execute(b1.getName());
                                Toast.makeText(getApplicationContext(), "Deleted : "+b1.getName(), Toast.LENGTH_LONG).show();
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
                        BucketMaker b = new BucketMaker(){
                            @Override
                            protected void onPostExecute(MinioClient minioClient) {
                                bucketsList = bucketsRetrived;
                                loadList();
                            }
                        };b.execute(edittext.getText().toString());
                        Toast.makeText(getApplicationContext(), "Created : "+edittext.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater infl = getMenuInflater();
        infl.inflate(R.menu.meniu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optiunea1:
                Intent intent1 = new Intent(this, ViewCloudBucketsActivity.class);
                startActivity(intent1);
                break;
            case R.id.optiunea2:

                pgd = new ProgressDialog(DriveFirstActivity.this);
                pgd.setMessage("Please wait...");
                pgd.setCanceledOnTouchOutside(true);
                pgd.show();
                Intent intent2 = new Intent(this, MapsActivity.class);
                startActivity(intent2);
                break;
        }

        return true;
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

}