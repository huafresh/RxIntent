package com.hua.rxintent;

import android.content.Intent;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
class RxIntentResult {

    int type;
    Intent data;

    RxIntentResult(int type, Intent data) {
        this.type = type;
        this.data = data;
    }
}
