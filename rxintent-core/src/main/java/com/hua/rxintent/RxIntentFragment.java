package com.hua.rxintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
@SuppressWarnings("ConstantConditions")
public class RxIntentFragment extends Fragment {

    /** 拍照后原始图片存储的名称 */
    private static final String TEMP_FILE_RELATIVE_PATH = "camera_temp_file.jpg";
    private String cameraPath;

    /** 裁剪后的图片存储的相对路径 */
    static final String CROP_FILE_PATH = "/cache/crop_file.jpg";

    public static final int INTENT_TYPE_CAMERA = 0;
    public static final int INTENT_TYPE_ALBUM = 1;
    public static final int INTENT_TYPE_CROP = 2;
    private Activity activity;

    static PublishSubject<RxIntentResult> resultSubject = PublishSubject.create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public static RxIntentFragment newInstance() {
        Bundle args = new Bundle();
        RxIntentFragment fragment = new RxIntentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Observable<String> openCamera() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                startActivityForResult(IntentUtil.getCameraIntent(activity), INTENT_TYPE_CAMERA);
                final Disposable disposable = resultSubject.subscribe(new RxIntentConsumer());
            }
        });
    }

    class RxIntentConsumer implements Consumer<RxIntentResult>{


        @Override
        public void accept(RxIntentResult intentResult) throws Exception {
            if (INTENT_TYPE_CAMERA == intentResult.type) {
                //emitter.onNext(IntentUtil.getCameraPath(activity));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_TYPE_CAMERA:
                resultSubject.onNext(new RxIntentResult(INTENT_TYPE_CAMERA, data));
                break;
            case INTENT_TYPE_ALBUM:
                resultSubject.onNext(new RxIntentResult(INTENT_TYPE_ALBUM, data));
                break;
            case INTENT_TYPE_CROP:
                resultSubject.onNext(new RxIntentResult(INTENT_TYPE_CROP, data));
                break;
            default:
                break;
        }

    }
}
