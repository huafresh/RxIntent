package com.hua.rxintent;

import android.content.Intent;

/**
 * @author hua
 * @version 2018/11/7 17:58
 */

class IntentRequest {
    private Intent intent;
    private String[] permissions;
    private IResult<Intent> callback;
    int requestCode;

    IntentRequest(Intent intent,
                  IResult<Intent> callback,
                  String[] permissions) {
        this.intent = intent;
        this.callback = callback;
        this.permissions = permissions;
    }

    Intent getIntent() {
        return intent;
    }

    IResult<Intent> getCallback() {
        return callback;
    }

    String[] getPermissions() {
        return permissions;
    }
}
