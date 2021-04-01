package com.example.myminiodrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.minio.messages.Bucket;

public class CustomAdapter extends ArrayAdapter<MyBuckets> {
    private Context cnt;
    private int resurse;
    private List<MyBuckets> bucketList;
    private LayoutInflater layoutInflater;

    public CustomAdapter(@NonNull Context context, int resource, List<MyBuckets> list, LayoutInflater linf){
        super(context, resource, list);
        this.cnt=context;
        this.resurse=resource;
        this.bucketList=list;
        this.layoutInflater=linf;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view=layoutInflater.inflate(resurse, parent, false);

        MyBuckets bk=bucketList.get(position);
        if (bk!=null){
            TextView tv1 = view.findViewById(R.id.textViewBucketName);
            tv1.setText(bk.getName().toString());
            TextView tv2 = view.findViewById(R.id.textViewBucketDate);
            tv2.setText(bk.getDate().toString());
        }

        return view;
    }
}
