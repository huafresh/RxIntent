package com.hua.rxintent;

import android.content.Intent;
import android.support.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/21 14:53
 */

abstract class RxIntentObserver<T> implements Observer<Intent>, IResultCallback<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Intent intent) {

    }

    @Override
    public void onComplete() {

    }
}
