package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
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
    public static final int REQUEST_CODE=200;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_second);

        listView=findViewById(R.id.listViewObjects);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton btn = findViewById(R.id.floatingActionButton2);

        Intent intent=getIntent();

        name=(String) intent.getSerializableExtra(DriveFirstActivity.BUCKET_NAME);

        ObjectsGetter o = new ObjectsGetter(){
            @Override
            protected void onPostExecute(MinioClient minioClient) {
                objectList = objectRetrived;
                loadList();
            }
        };o.execute(name);

        /***
         * This is the code used in order to download an object
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Objects b1=objectList.get(position);
                AlertDialog dialog=new AlertDialog.Builder(DriveSecondActivity.this).setTitle("Download Object")
                        .setMessage("Are you sure you want to download the object?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                Toast.makeText(getApplicationContext(), "The object was not downloaded", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                ObjectsDownloader o = new ObjectsDownloader(){
                                    @Override
                                    protected void onPostExecute(MinioClient minioClient) {

                                    }
                                };o.execute(name, b1.getName());
                                Toast.makeText(getApplicationContext(), "Downloaded: "+b1.getName() + " to /sdcard/Download/", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).create();

                dialog.show();
            }
        });

        /***
         * This is the code used to delete an object
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectCustomAdapter adapter = (ObjectCustomAdapter) listView.getAdapter();
                Objects b1=objectList.get(position);

                AlertDialog dialog=new AlertDialog.Builder(DriveSecondActivity.this).setTitle("Delete Object")
                        .setMessage("Are you sure you want to delete this object?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                Toast.makeText(getApplicationContext(), "The object was not deleted...", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                ObjectsRemover o = new ObjectsRemover(){
                                    @Override
                                    protected void onPostExecute(MinioClient minioClient) {
                                        objectList = objectRetrived;
                                        loadList();
                                    }
                                };o.execute(name, b1.getName());
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
                Intent intent=new Intent(getApplicationContext(), FileExplorerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        /***
         * This is where the search bar magic happens
         */
        EditText myTextBox= findViewById(R.id.editTextSearchBar);
        myTextBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    try {
                        ObjectsGetter o = new ObjectsGetter(){
                            @Override
                            protected void onPostExecute(MinioClient minioClient) {
                                objectList = objectRetrived;
                                loadList();
                            }
                        };o.execute(name);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                ObjectsSearch o = new ObjectsSearch(){
                    @Override
                    protected void onPostExecute(MinioClient minioClient) {
                        objectList = objectRetrived;
                        loadList();
                    }
                };o.execute(name, s.toString());
            }
        });

        Button chart = findViewById(R.id.chartButton);

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), StatsActivity.class);
                intent.putExtra("bucketName", name);
                startActivity(intent);
            }
        });

    }

    /***
     * This loads the list of objects
     */
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

    /***
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            String filename = (String) data.getSerializableExtra(FileExplorerActivity.ADD_FILE);

            String path = "/sdcard/Download/" + filename;

            ObjectsMaker o = new ObjectsMaker(){
                @Override
                protected void onPostExecute(MinioClient minioClient) {
                    objectList = objectRetrived;
                    loadList();
                }
            };o.execute(name, filename, path);
            Toast.makeText(getApplicationContext(), "Uploaded : "+filename + " successfully", Toast.LENGTH_LONG).show();
        }
    }
}