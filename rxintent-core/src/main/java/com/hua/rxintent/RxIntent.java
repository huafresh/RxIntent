package com.hua.rxintent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
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

    static final int INTENT_TYPE_CAMERA = 0;
    static final int INTENT_TYPE_ALBUM = 1;
    static final int INTENT_TYPE_CROP = 2;

    public static RxIntentObservable openCamera(final FragmentActivity activity) {
        IIntentBuilder builder = getIntentBuilderByType(INTENT_TYPE_CAMERA);
        final Intent intent = builder.build(activity);
        Observable<Intent> source = Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(final ObservableEmitter<Intent> emitter) throws Exception {
                final IntentRequest request = new IntentRequest(INTENT_TYPE_CAMERA, intent, new IResult() {
                    @Override
                    public void onResult(@Nullable Intent data) {
                        emitter.onNext(data);
                    }
                });
                RxIntentFragment.openByFragment(activity, request);
            }
        });
        return new RxIntentObservable(source);
    }

    static IIntentBuilder getIntentBuilderByType(int type) {
        switch (type) {
            case INTENT_TYPE_CAMERA:
                return new CameraIntent();
            default:
                throw new IllegalArgumentException("unSupport intent type");
        }
    }

}
