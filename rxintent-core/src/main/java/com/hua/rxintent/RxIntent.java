package com.hua.rxintent;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
public class RxIntent {

    public static RxIntentObservable<String> openCamera(final FragmentActivity activity) {
        return openInternal(activity, new CameraIntent());
    }

    public static RxIntentObservable<String> openAlbum(final FragmentActivity activity) {
        return openInternal(activity, new AlbumIntent());
    }

    public static RxIntentObservable<String> openCrop(final FragmentActivity activity, String filePath) {
        return openInternal(activity, new CropIntent(filePath));
    }

    public static RxIntentObservable<String> openCrop(final FragmentActivity activity, Uri uri) {
        return openInternal(activity, new CropIntent(uri));
    }

    public static RxIntentObservable<String> openCameraAndCrop(final FragmentActivity activity) {
        return openCamera(activity)
                .flatMap(new IConverter<String, RxIntentObservable<String>>() {
                    @Override
                    public RxIntentObservable<String> convert(String path) {
                        return openCrop(activity, path);
                    }
                });
    }

    public static RxIntentObservable<String> openAlbumAndCrop(final FragmentActivity activity) {
        return openAlbum(activity)
                .flatMap(new IConverter<String, RxIntentObservable<String>>() {
                    @Override
                    public RxIntentObservable<String> convert(String path) {
                        return openCrop(activity, path);
                    }
                });
    }

    @NonNull
    private static <T> RxIntentObservable<T> openInternal(final FragmentActivity activity,
                                                          final AbstractIntent<Intent, T> absIntent) {
        return new RxIntentObservable<T>(activity, absIntent).setDefaultConverter(absIntent);
    }

    public static void enableDebug() {
        Util.debugEnable = true;
    }

    public static void disableDebug() {
        Util.debugEnable = false;
    }


}
