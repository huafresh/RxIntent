package com.hua.rxintent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author hua
 * @version 2018/11/7 17:29
 */

class CameraIntent extends AbstractIntent<Intent, String> {

    /**
     * 拍照后原始图片存储的名称
     */
    private static final String TEMP_FILE_NAME = "camera_temp.jpg";
    private static String savePath;

    @Override
    public Intent build(Context context) {
        String dirPath = Util.getCacheDirPath(context);
        File saveFile = Util.createFileIfNeed(context, dirPath, TEMP_FILE_NAME);
        if (saveFile != null) {
            savePath = saveFile.getAbsolutePath();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getCompatUriFromFile(context, saveFile));
            if (Util.isN()) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            return intent;
        }
        return null;
    }

    @Override
    String[] needPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            return new String[0];
        }
    }


    @Override
    public String convert(Intent intent) {
        return savePath;
    }
}
