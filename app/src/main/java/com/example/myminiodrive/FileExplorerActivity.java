package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileExplorerActivity extends AppCompatActivity {

    public static final String ADD_FILE="addingFile";
    List<String>filesList= new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        listView=findViewById(R.id.listViewFiles);

        filesList = Arrays.asList(Objects.requireNonNull(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS)
                .list()));
        Intent intent=getIntent();
        loadList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename=filesList.get(position);
                intent.putExtra(ADD_FILE, filename);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void loadList(){
        FilesCustomAdapter adapter=new FilesCustomAdapter(getApplicationContext(), R.layout.files_list_view, filesList, getLayoutInflater()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                View view=super.getView(position ,convertView, parent);
                String file = filesList.get(position);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }
}