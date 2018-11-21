package com.hua.rxintent;

import android.content.Intent;

/**
 * @author hua
 * @version 2018/11/7 17:58
 */

class IntentRequest {
    private Intent intent;
    private String[] permissions;
    private IResultCallback<Intent> callback;
    int requestCode;
    Object result;

    IntentRequest(Intent intent,
                  String[] permissions,
                  IResultCallback<Intent> callback) {

        this.intent = intent;
        this.callback = callback;
        this.permissions = permissions;
    }

    Intent getIntent() {
        return intent;
    }

    IResultCallback<Intent> getCallback() {
        return callback;
    }

    String[] getPermissions() {
        return permissions;
    }
}
