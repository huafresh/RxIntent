package com.hua.rxintent;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

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
    protected void subscribeActual(Observer<? super U> observer) {
        source.subscribe(new MergeObserver<>(observer, converter));
    }

    static final class MergeObserver<T, U> implements Disposable, Observer<T> {
        private final IConverter<T, RxIntentObservable<U>> converter;
        private Observer<? super U> actual;
        private boolean cancelled = false;
        private Disposable s;

        private MergeObserver(Observer<? super U> actual,
                              IConverter<T, RxIntentObservable<U>> converter) {
            this.actual = actual;
            this.converter = converter;
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(s, d)) {
                s = d;
                actual.onSubscribe(this);
            }
        }

        @Override
        public void onNext(T t) {
            RxIntentObservable<U> p = null;
            try {
                p = converter.convert(t);
            } catch (Exception e) {
                s.dispose();
                onError(e);
            }
            if (p != null) {
                p.subscribe(new Observer<U>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(U u) {
                        actual.onNext(u);
                    }

                    @Override
                    public void onError(Throwable e) {
                        actual.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        actual.onComplete();
                    }
                });
            } else {
                onError(new NullPointerException("The mapper returned a null ObservableSource"));
            }

        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void dispose() {
            if (!cancelled) {
                cancelled = true;
                s.dispose();
            }
        }

        @Override
        public boolean isDisposed() {
            return cancelled;
        }
    }
}
