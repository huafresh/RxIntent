package com.hua.rxintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxIntent.openCamera(this)
                .before(new IInterceptor() {
                    @Override
                    public void intercept(Intent intent) {

                    }
                })
                .after(new IInterceptor() {
                    @Override
                    public void intercept(Intent intent) {

                    }
                })
                .subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Exception {

            }
        })

    }
}
