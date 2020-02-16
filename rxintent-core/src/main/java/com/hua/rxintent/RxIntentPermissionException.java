package com.hua.rxintent;

/**
 * @author zhangsh
 * @version V1.0
 * @date 2020-02-16 11:37
 */

public class RxIntentPermissionException extends Exception {

    private String[] deniedPermission;

    public RxIntentPermissionException(String message, String[] deniedPermission) {
        super(message);
        this.deniedPermission = deniedPermission;
    }

    public String[] getDeniedPermission() {
        return deniedPermission;
    }
}
