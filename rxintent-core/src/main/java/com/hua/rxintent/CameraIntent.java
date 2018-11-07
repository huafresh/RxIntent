package com.hua.rxintent;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author hua
 * @version 2018/11/7 17:29
 */

class CameraIntent implements IIntentBuilder {

    /** 拍照后原始图片存储的名称 */
    private static final String TEMP_FILE_RELATIVE_PATH = "camera_temp_file.jpg";
    private static String cameraPath;

    @Override
    public Intent build(Context context) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ensureCameraPath(context);

        File file = new File(cameraPath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getUriFromFile(context, file));

        if (Util.isN()) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return intent;
    }

    String getCameraPath(){
        return cameraPath;
    }

    @SuppressWarnings("ConstantConditions")
    private static void ensureCameraPath(Context context) {
        if (TextUtils.isEmpty(cameraPath)) {
            if (Util.isExternalExist()) {
                cameraPath = context.getExternalCacheDir().getAbsolutePath() +
                        File.separator + TEMP_FILE_RELATIVE_PATH;
            } else {
                cameraPath = context.getCacheDir().getAbsolutePath() +
                        File.separator + TEMP_FILE_RELATIVE_PATH;
            }
        }
    }
}
