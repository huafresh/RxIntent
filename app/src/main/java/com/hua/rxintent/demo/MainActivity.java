package com.hua.rxintent.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hua.rxintent.RxIntent;

import io.reactivex.functions.Consumer;

@SuppressLint("CheckResult")
public class MainActivity extends AppCompatActivity {
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera = findViewById(R.id.btn_camera);
        Button album = findViewById(R.id.btn_album);
        Button crop = findViewById(R.id.btn_crop);
        final ImageView imageView = findViewById(R.id.image);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxIntent.openCamera(MainActivity.this)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String data) throws Exception {
                                Log.e("@@@hua", "camera path = " + data);
                                Bitmap bitmap = BitmapFactory.decodeFile(data);
                                imageView.setImageBitmap(bitmap);
                                path = data;
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxIntent.openCamera(MainActivity.this)
                        .asIntent()
                        .subscribe(new Consumer<Intent>() {
                            @Override
                            public void accept(Intent intent) throws Exception {
                                Log.e("@@@hua", "album origin intent = " + intent);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("@@@hua", "album failed. = " + throwable.toString());
                            }
                        });
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_camera_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxIntent.openCameraAndCrop(MainActivity.this)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.e("@@@hua", "camera and crop, path = " + s);
                            }
                        });
            }
        });

        findViewById(R.id.btn_album_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
