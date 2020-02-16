package com.hua.rxintent;

import androidx.annotation.Nullable;

/**
 * @author hua
 * @version 2018/11/7 16:21
 */

interface IResultCallback<T> {
    /**
     * 权限被拒绝
     *
     * @param permissions 被拒绝的权限
     */
    void onPermissionsDenied(String[] permissions);

    /**
     * 流程正常，并且resultCode = Activity.RESULT_OK
     *
     * @param data 从目标页面返回的结果
     */
    void onResult(@Nullable T data);

    /**
     * 流程异常
     *
     * @param t 异常信息
     */
    void onError(Throwable t);
}
