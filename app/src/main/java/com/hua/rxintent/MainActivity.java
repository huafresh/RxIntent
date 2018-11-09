package com.hua.rxintent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                        .subscribe(new IResult<String>() {
                            @Override
                            public void onResult(String data) {
                                Log.e("@@@hua", "camera path = " + data);
                                Bitmap bitmap = BitmapFactory.decodeFile(data);
                                imageView.setImageBitmap(bitmap);
                                path = data;
                            }
                        });
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxIntent.openAlbum(MainActivity.this)
                        .subscribe(new IResult<String>() {
                            @Override
                            public void onResult(String data) {
                                Log.e("@@@hua", "album path = " + data);
                                Bitmap bitmap = BitmapFactory.decodeFile(data);
                                path = data;
                                imageView.setImageBitmap(bitmap);
                            }
                        });
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxIntent.openCrop(MainActivity.this, path)
                        .subscribe(new IResult<String>() {
                            @Override
                            public void onResult(String data) {
                                Log.e("@@@hua", "crop path = " + data);
                                Bitmap bitmap = BitmapFactory.decodeFile(data);
                                imageView.setImageBitmap(bitmap);
                            }
                        });
            }
        });

    }
}
