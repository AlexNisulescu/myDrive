package com.example.myminiodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import io.minio.messages.Progress;

public class StatsActivity extends AppCompatActivity {

    String name;
    private MinioClient minioClient;
    private List<Objects> objectList= new ArrayList<>();
    private List<StatObjectResponse> stats = new ArrayList<>();
    private int usage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Intent intent=getIntent();

        name=(String) intent.getSerializableExtra("bucketName");
        try {
            objectsGetter(name);
            statsGetter(name);
        } catch (IOException | InvalidKeyException | InvalidResponseException |
                InsufficientDataException | NoSuchAlgorithmException |
                ServerException | InternalException | XmlParserException |
                ErrorResponseException e) {
            e.printStackTrace();
        }

        List<Double> Size = new ArrayList<>();
        for(Objects obj : objectList)
            Size.add(Double.valueOf(obj.getSize()));

        XYPlot plot = findViewById(R.id.mySimpleXYPlot);

        plot.setTitle("Occupied Space graphic");

        XYSeries series1 = new SimpleXYSeries(Size, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Occupied");
        plot.addSeries(series1, new LineAndPointFormatter(getApplicationContext(), R.layout.f1));

        ProgressBar pb = findViewById(R.id.stats_progressbar);
        TextView tv = findViewById(R.id.usageTextView);

        int imagesCount = 0;
        int othersCount = 0;

        for (int i =0;i<stats.size();i++){
            String type = stats.get(i).contentType();
            if (type.equals("image/jpeg")){
                imagesCount++;
            }
            else{
                othersCount++;
            }
        }
        othersCount+=imagesCount;
        int percentage = imagesCount*100/othersCount;
        pb.setProgress(percentage);
        tv.setText(imagesCount + " / " + othersCount);

    }

    public void usageCalculator(){
        for (Objects obj : objectList ) {
            usage+=Integer.parseInt(obj.getSize());
        }
        System.out.println(usage);
    }

    public void statsGetter(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        for (Objects obj : objectList ){
            StatObjectResponse objectStat =
                    minioClient.statObject(
                            StatObjectArgs.builder().bucket(name).object(obj.getName()).build());
            stats.add(objectStat);
        }
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

}