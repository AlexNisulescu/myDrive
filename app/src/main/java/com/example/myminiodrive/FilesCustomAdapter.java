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

public class FilesCustomAdapter extends ArrayAdapter<String> {
    private Context cnt;
    private int resurse;
    private List<String> filesList;
    private LayoutInflater layoutInflater;

    public FilesCustomAdapter(@NonNull Context context, int resource, List<String> list, LayoutInflater linf){
        super(context,resource, list);
        this.cnt=context;
        this.resurse=resource;
        this.filesList=list;
        this.layoutInflater=linf;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view=layoutInflater.inflate(resurse, parent, false);

        String obj = filesList.get(position);
        if (obj!=null){
            TextView tv1 = view.findViewById(R.id.textViewFiles);
            tv1.setText(obj);
        }

        return view;
    }
}
