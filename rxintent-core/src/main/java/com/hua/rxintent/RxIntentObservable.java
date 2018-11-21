package com.hua.rxintent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author hua
 * @version 2018/11/7 17:15
 */

public class RxIntentObservable<T> extends Observable<Intent> implements IResultCallback<Intent> {
    private Intent intent;
    private IConverter<Intent, T> defaultConverter;
    private AbstractIntent<Intent, T> absIntent;
    private FragmentActivity activity;
    private Observer<? super Intent> observer;


    RxIntentObservable(FragmentActivity activity,
                       AbstractIntent<Intent, T> absIntent) {
        this.activity = activity;
        this.absIntent = absIntent;
        this.intent = absIntent.build(activity);
    }

    /**
     * 拦截Intent请求
     *
     * @param converter 转换器
     * @return this
     */
    public RxIntentObservable<T> beforeStart(IConverter<Intent, Intent> converter) {
        this.intent = converter.convert(intent);
        return this;
    }

    RxIntentObservable<T> setDefaultConverter(IConverter<Intent, T> converter) {
        this.defaultConverter = converter;
        return this;
    }

    /**
     * {@link Observable#subscribe}的包装。
     * 使用此方法会使用默认的转换器转换结果。
     *
     * @param callback 结果回调
     */
    public void subscribe(final IResultCallback<T> callback) {
        subscribe(new RxIntentObserver<Intent>() {
            @Override
            public void onPermissionsDenied(String[] permissions) {
                callback.onPermissionsDenied(permissions);
            }

            @Override
            public void onResult(@Nullable Intent data) {
                callback.onResult(defaultConverter.convert(data));
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    protected void subscribeActual(Observer<? super Intent> observer) {
        this.observer = observer;
        final IntentRequest request = new IntentRequest(intent,
                absIntent.needPermissions(), this);
        RxIntentFragment.enqueueRequest(activity, request);
    }

    @Override
    public void onPermissionsDenied(String[] permissions) {
        if (observer instanceof IResultCallback) {
            ((IResultCallback) observer).onPermissionsDenied(permissions);
        } else {
            Util.e("start intent failed. permission denied");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResult(@Nullable Intent data) {
        if (observer instanceof IResultCallback) {
            ((IResultCallback) observer).onResult(data);
        } else {
            observer.onNext(data);
        }
    }

    @Override
    public void onError(Throwable t) {
        observer.onError(t);
    }

}
