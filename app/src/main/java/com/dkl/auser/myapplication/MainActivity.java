package com.dkl.auser.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private Button btnClick;
    private ProgressBar progressbar1;
    private ImageView imageView;
    private String urlPath = "";
    private String[] urls = {"https://upload.wikimedia.org/wikipedia/commons/e/e0/JPEG_example_JPG_RIP_050.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/7/7c/Hawk_eating_prey.jpg",
            "http://cpansearch.perl.org/src/AGRUNDMA/Image-Scale-0.11/t/images/jpg/exif_mirror_horiz.jpg"};

    private byte[] data = null;
    private int loadindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClick = (Button) findViewById(R.id.buttonClick);
        progressbar1 = (ProgressBar) findViewById(R.id.progressBar1);
        imageView = (ImageView) findViewById(R.id.ivImage);

        btnClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnClick.setClickable(false);
                if (loadindex == urls.length) {
                    loadindex = 0;
                }
                urlPath = urls[loadindex];
                loadindex++;
                data = null;
                progressbar1.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            data = loadData();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressbar1.setVisibility(View.GONE);

                                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                imageView.setImageBitmap(bm);
                                imageView.setVisibility(View.VISIBLE);
                                btnClick.setClickable(true);
                            }
                        });
                    }
                });
                thread.start();
            }
        });

    }

    private byte[] loadData() throws Exception {

        InputStream inputstream;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        inputstream = conn.getInputStream();
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputstream.read(buffer)) != -1) {
            outputstream.write(buffer, 0, len);
        }
        inputstream.close();
        outputstream.close();
        return outputstream.toByteArray();

    }
}