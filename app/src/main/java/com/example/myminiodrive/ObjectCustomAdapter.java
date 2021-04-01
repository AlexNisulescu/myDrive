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

import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

public class ObjectCustomAdapter extends ArrayAdapter<Objects> {
    private Context cnt;
    private int resurse;
    private List<Objects> itemsList;
    private LayoutInflater layoutInflater;

    public ObjectCustomAdapter(@NonNull Context context, int resource, List<Objects> list, LayoutInflater linf){
        super(context,resource, list);
        this.cnt=context;
        this.resurse=resource;
        this.itemsList=list;
        this.layoutInflater=linf;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view=layoutInflater.inflate(resurse, parent, false);

        Objects obj = itemsList.get(position);
        if (obj!=null){
            TextView tv1 = view.findViewById(R.id.textViewObjectName);
            tv1.setText(obj.getName());
            TextView tv2 = view.findViewById(R.id.textViewObjectSize);
            tv2.setText(obj.getSize());
            TextView tv3 = view.findViewById(R.id.textViewObjectDate);
            tv3.setText(obj.getDate());
        }

        return view;
    }
}
