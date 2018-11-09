package com.hua.rxintent;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * @author hua
 * @version 2018/11/7 16:21
 */

public interface IResult<T> {
    void onResult(T data);
}
