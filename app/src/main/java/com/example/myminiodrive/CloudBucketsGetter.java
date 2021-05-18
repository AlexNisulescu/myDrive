package com.example.myminiodrive;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CloudBucketsGetter extends AsyncTask<URL, Void, String> {
    public List<MyBuckets> bucketsRetrived;
    JSONArray buckets = null;

    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            InputStream ist = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader br = new BufferedReader(isr);
            String linie= null;
            String buffer = "";
            while((linie=br.readLine())!=null)
                buffer+=linie;

            parseJSON(buffer);

            return buffer;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private void parseJSON(String jsonStr){
        bucketsRetrived =  new ArrayList<>();
        if (jsonStr!=null){
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                buckets = jsonobj.getJSONArray("buckets");

                for (int i=0;i<buckets.length();i++){
                    JSONObject obj = buckets.getJSONObject(i);

                    String name = obj.getString("Name");

                    ZonedDateTime creationDate = ZonedDateTime.parse(obj.getString("CreationDate"));
                    MyBuckets bk = new MyBuckets(name, creationDate);
                    bucketsRetrived.add(bk);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e("parseJSON", "JSON IS NULL");
        }
    }
}
