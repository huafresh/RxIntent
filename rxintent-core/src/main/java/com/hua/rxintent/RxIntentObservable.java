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
    private IConverter<Intent, T> defaultConverter;
    IResultCallback<T> callback;

    RxIntentObservable(Observable<Intent> source, Intent intent) {
        this.source = source;
        this.intent = intent;
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
     * 会触发真正启动intent，并且使用默认转换器转换结果。
     *
     * @param result 结果回调
     * @return rx Disposable
     */
    public Disposable subscribe(final IResultCallback<T> result) {
        return source.subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Exception {
                result.onResult(defaultConverter.convert(intent));
            }
        });
    }

    /**
     * {@link Observable#subscribe}的包装。
     * 会触发真正启动intent，不使用任何转换器，所以拿到的是原始返回的Intent。
     *
     * @param result 结果回调
     * @return rx Disposable
     */
    public Disposable subscribeWithoutConvert(final IResultCallback<Intent> result) {
        return source.subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Exception {
                result.onResult(intent);
            }
        });
    }
}
