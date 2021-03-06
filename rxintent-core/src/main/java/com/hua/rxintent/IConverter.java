package com.hua.rxintent;

import android.content.Intent;

/**
 * @author hua
 * @version 2018/11/7 17:17
 */

public interface IConverter<R, T> {
    /**
     * 对象R转T
     */
    T convert(R intent);
}
