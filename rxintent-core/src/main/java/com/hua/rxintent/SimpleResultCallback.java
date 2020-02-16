package com.hua.rxintent;

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
