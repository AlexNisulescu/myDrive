package com.example.myminiodrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewCloudBucketsActivity extends AppCompatActivity {
    private List<MyBuckets> bucketsList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cloud_buckets);
        listView = findViewById(R.id.listViewBuckets2);

        CloudBucketsGetter b = new CloudBucketsGetter(){
            @Override
            protected void onPostExecute(String s) {
                bucketsList = bucketsRetrived;
                loadList();
            }
        };try {
            b.execute(new URL("https://pastebin.com/raw/g4JAT4Jf"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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