package com.hua.rxintent;

import android.content.Intent;
import android.support.annotation.Nullable;

import io.reactivex.Observer;

/**
 * 仅仅用于使flatMap方法返回RxIntentObservable
 *
 * @author hua
 * @version V1.0
 * @date 2018/11/21 15:49
 */

class FlatMapObservable<T, U> extends RxIntentObservable<U> {

    private RxIntentObservable<T> source;
    private IConverter<T, RxIntentObservable<U>> converter;

    FlatMapObservable(RxIntentObservable<T> source,
                      IConverter<T, RxIntentObservable<U>> converter) {
        super();
        this.source = source;
        this.converter = converter;
    }

    @Override
    protected void subscribeActual(Observer<? super Intent> observer) {
        source.subscribe2(new MergeObserver<>(observer, converter));
    }

    static final class MergeObserver<T, U> implements IResultCallback<T> {
        private final IConverter<T, RxIntentObservable<U>> converter;
        private Observer actual;

        private MergeObserver(Observer actual,
                              IConverter<T, RxIntentObservable<U>> converter) {
            this.actual = actual;
            this.converter = converter;
        }

        @Override
        public void onPermissionsDenied(String[] permissions) {
            if (actual instanceof IResultCallback) {
                ((IResultCallback) actual).onPermissionsDenied(permissions);
            }
        }

        @Override
        public void onResult(@Nullable T data) {
            converter.convert(data).subscribe2(new InnerObserver<U>(actual));
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }
    }

    static final class InnerObserver<U> implements IResultCallback<U> {
        private Observer parent;

        InnerObserver(Observer parent) {
            this.parent = parent;
        }

        @Override
        public void onPermissionsDenied(String[] permissions) {
            if (parent instanceof IResultCallback) {
                ((IResultCallback) parent).onPermissionsDenied(permissions);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onResult(@Nullable U data) {
            if (parent instanceof IResultCallback) {
                ((IResultCallback) parent).onResult(data);
            } else {
                parent.onNext(data);
            }
        }

        @Override
        public void onError(Throwable t) {
            parent.onError(t);
        }
    }
}
