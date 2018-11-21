package com.hua.rxintent;

import android.support.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/21 10:33
 */

public abstract class SimpleResultCallback<T> implements IResultCallback<T> {
    @Override
    public void onPermissionsDenied(String[] permissions) {

    }

    @Override
    public void onError(Throwable t) {

    }
}
