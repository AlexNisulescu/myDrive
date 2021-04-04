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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
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
    private InputStream in;
    private ByteArrayOutputStream out;
    private byte[] response;
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

        try {
            objectsGetter(name);
        } catch (IOException | InvalidKeyException | InvalidResponseException |
                InsufficientDataException | NoSuchAlgorithmException |
                ServerException | InternalException |
                XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
        }
        if (!objectList.isEmpty()){
            loadList();
        }

        /***
         * This is the code used in order to download an object
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Objects b1=objectList.get(position);
                AlertDialog dialog=new AlertDialog.Builder(DriveSecondActivity.this).setTitle("Confirmati descarcarea")
                        .setMessage("Doriti descarcarea")
                        .setNegativeButton("NU", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {
                                Toast.makeText(getApplicationContext(), "Datele nu au fost descarcate", Toast.LENGTH_LONG).show();
                                dialogInt.cancel();
                            }
                        }).setPositiveButton("DA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInt, int which) {

                                try {
                                    in = new BufferedInputStream(minioClient
                                            .getObject(GetObjectArgs.builder()
                                                    .bucket(name).object(b1
                                                            .getName()).
                                                            build()));
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
                                        FileOutputStream fos = new FileOutputStream("/sdcard/Download/"+b1.getName());
                                        fos.write(response);
                                        fos.close();
                                    }
                                    catch(IOException e){
                                        e.printStackTrace();
                                    }

                                } catch (ErrorResponseException |
                                        InsufficientDataException |
                                        InternalException | InvalidKeyException |
                                        InvalidResponseException | IOException |
                                        NoSuchAlgorithmException |
                                        ServerException |
                                        XmlParserException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), "Am descarcat: "+b1.getName(), Toast.LENGTH_LONG).show();
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

                AlertDialog dialog=new AlertDialog.Builder(DriveSecondActivity.this).setTitle("Confirmare Stergere")
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
                                    minioClient.removeObject(
                                            RemoveObjectArgs.builder().bucket(name).object(b1.getName()).build());
                                } catch (ErrorResponseException |
                                        InsufficientDataException |
                                        InternalException | InvalidKeyException |
                                        InvalidResponseException |
                                        IOException |
                                        NoSuchAlgorithmException |
                                        ServerException |
                                        XmlParserException e) {
                                    e.printStackTrace();
                                }
                                objectList.remove(b1);
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
                        objectList.clear();
                        objectsGetter(name);
                        loadList();
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
                Iterable<Result<Item>> results;
                results = minioClient.listObjects(
                        ListObjectsArgs.builder().bucket(name).recursive(true).prefix((s.toString())).build());

                for(Result<Item> res : results){
                    try {
                        objectList.clear();
                        ObjectCustomAdapter adapter = (ObjectCustomAdapter) listView.getAdapter();
                        Item item = res.get();
                        Objects obj=new Objects(item.objectName(), item.size(), item.lastModified());
                        objectList.add(obj);
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
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
     * This gets all the objects from the server
     * @param bucketName
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public void objectsGetter(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results;
        minioClient =
                MinioClient.builder()
                        .endpoint("10.0.2.2", 9000, false)
                        .credentials("myminio", "myminio123")
                        .build();
        results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).build());

        for(Result<Item> res : results){
            Item item=res.get();
            Objects obj=new Objects(item.objectName(), item.size(), item.lastModified());
            objectList.add(obj);
        }
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

            try {
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(name).object(filename).filename(path).build());
                objectList.clear();
                objectsGetter(name);
                loadList();
            } catch (ErrorResponseException | InsufficientDataException |
                    InternalException | InvalidKeyException |
                    InvalidResponseException | IOException |
                    NoSuchAlgorithmException | ServerException |
                    XmlParserException e) {
                e.printStackTrace();
            }

        }
    }
}