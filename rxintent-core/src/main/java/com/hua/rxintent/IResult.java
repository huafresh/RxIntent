package com.hua.rxintent;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * @author hua
 * @version 2018/11/7 16:21
 */

public interface IResult {
    /**
     * 结果回调
     *
     * @param data 不为空表示正常
     */
    void onResult(@Nullable Intent data);
}
