package com.hua.rxintent;

import android.content.Intent;

/**
 * @author hua
 * @version 2018/11/7 17:58
 */

class IntentRequest {
    private int type;
    private Intent intent;
    private IResult callback;

    IntentRequest(int type, Intent intent, IResult callback) {
        this.type = type;
        this.intent = intent;
        this.callback = callback;
    }

    public int getType() {
        return type;
    }

    public Intent getIntent() {
        return intent;
    }

    public IResult getCallback() {
        return callback;
    }
}
