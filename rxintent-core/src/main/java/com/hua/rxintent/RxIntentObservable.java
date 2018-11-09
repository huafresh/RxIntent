package com.hua.rxintent;

import android.content.Intent;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author hua
 * @version 2018/11/7 17:15
 */

public class RxIntentObservable<T> {
    private Observable<Intent> source;
    private Intent intent;
    private IResultHandler<Intent, T> converter;

    public RxIntentObservable(Observable<Intent> source, Intent intent) {
        this.source = source;
        this.intent = intent;
    }

    public RxIntentObservable<T> beforeStart(IResultHandler<Intent, Intent> converter) {
        this.intent = converter.handle(intent);
        return this;
    }

    public Disposable onGetResult(final IResult<Intent> result) {
        return source.subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Exception {
                result.onResult(intent);
            }
        });
    }

     RxIntentObservable<T> setConverter(IResultHandler<Intent, T> converter) {
        this.converter = converter;
        return this;
    }

    public Disposable subscribe(final IResult<T> result) {
        return source.subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Exception {
                result.onResult(converter.handle(intent));
            }
        });
    }
}
