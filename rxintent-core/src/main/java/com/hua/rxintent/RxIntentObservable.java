package com.hua.rxintent;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author hua
 * @version 2018/11/7 17:15
 */

public class RxIntentObservable<T> extends Observable<T> implements Disposable {
    private Intent intent;
    private IConverter<Intent, T> defaultConverter;
    private AbstractIntent<Intent, T> absIntent;
    private FragmentActivity activity;
    private Observer<? super T> observer;
    private Intent data;
    private boolean cancelled = false;
    private IResultCallback<Intent> resultCallback = new IResultCallback<Intent>() {
        @Override
        public void onPermissionsDenied(String[] permissions) {
            if (!cancelled) {
                observer.onError(new RxIntentPermissionException("某些权限被拒绝", permissions));
            }
        }

        @Override
        public void onResult(@Nullable Intent data) {
            if (!cancelled) {
                RxIntentObservable.this.data = data;
                observer.onNext(defaultConverter.convert(data));
            }
        }

        @Override
        public void onError(Throwable t) {
            if (!cancelled) {
                observer.onError(t);
            }
        }
    };

    RxIntentObservable() {
    }

    RxIntentObservable(FragmentActivity activity,
                       AbstractIntent<Intent, T> absIntent) {
        this.activity = activity;
        this.absIntent = absIntent;
        this.intent = absIntent.build(activity);
    }

    /**
     * 可以通过这个方法自定义打开系统应用的参数。
     *
     * @param converter 转换器
     * @return this
     */
    public RxIntentObservable beforeStart(IConverter<Intent, Intent> converter) {
        this.intent = converter.convert(intent);
        return this;
    }

    /**
     * 转成系统应用返回的原始Intent
     */
    public Observable<Intent> asIntent() {
        return flatMap(new Function<T, Observable<Intent>>() {
            @Override
            public Observable<Intent> apply(T path) throws Exception {
                return Observable.just(data);
            }
        });
    }

    RxIntentObservable setDefaultConverter(IConverter<Intent, T> converter) {
        this.defaultConverter = converter;
        return this;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        this.observer = observer;
        observer.onSubscribe(this);
        final IntentRequest request = new IntentRequest(intent,
                absIntent.needPermissions(), resultCallback);
        RxIntentFragment.enqueueRequest(activity, request);
    }

    <U> RxIntentObservable flatMap2(final IConverter<T, RxIntentObservable<U>> converter) {
        return new FlatMapObservable<T, U>(this, converter);
    }

    @Override
    public void dispose() {
        if (!cancelled) {
            cancelled = true;
        }
    }

    @Override
    public boolean isDisposed() {
        return cancelled;
    }
}
