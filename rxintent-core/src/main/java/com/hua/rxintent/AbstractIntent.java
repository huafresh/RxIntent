package com.hua.rxintent;

import android.content.Context;
import android.content.Intent;

/**
 * @author hua
 * @version 2018/11/8 11:03
 */

abstract class AbstractIntent<R, T> implements IConverter<R, T> {
    abstract Intent build(Context context);

    abstract String[] needPermissions();
}
