package com.hua.rxintent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

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

    @NonNull
    private static <T> RxIntentObservable<T> openInternal(final FragmentActivity activity,
                                                          final AbstractIntent<Intent, T> absIntent) {
        return new RxIntentObservable<T>(activity, absIntent).setDefaultConverter(absIntent);
    }

}
