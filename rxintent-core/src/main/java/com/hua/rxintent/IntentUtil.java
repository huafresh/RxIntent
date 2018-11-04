package com.hua.rxintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * @author hua
 * @version 1.0
 * @date 2018/11/4
 */
@SuppressWarnings("ALL")
class IntentUtil {

    /** 拍照后原始图片存储的名称 */
    private static final String TEMP_FILE_RELATIVE_PATH = "camera_temp_file.jpg";
    private static String cameraPath;

    /** 裁剪后的图片存储的相对路径 */
    static final String CROP_FILE_PATH = "/cache/crop_file.jpg";

    static @Nullable
    Intent getCameraIntent(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ensureCameraPath(context);

        File file = new File(cameraPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "无法创建存储拍照结果的文件", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriFromFile(context, file));

        if (isN()) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return intent;
    }

    private static void ensureCameraPath(Context context) {
        if (TextUtils.isEmpty(cameraPath)) {
            if (isExternalExist()) {
                cameraPath = context.getExternalCacheDir().getAbsolutePath() +
                        File.separator + TEMP_FILE_RELATIVE_PATH;
            } else {
                cameraPath = context.getCacheDir().getAbsolutePath() +
                        File.separator + TEMP_FILE_RELATIVE_PATH;
            }
        }
    }

    private static Uri getUriFromFile(Context context, File file) {
        ApplicationInfo info = context.getApplicationInfo();
        if (isN() &&
                info.targetSdkVersion >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    info.packageName + ".rxintent.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private static boolean isN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    private static boolean isExternalExist() {
        return MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    static String getCameraPath(Context context){
        ensureCameraPath(context);
        return cameraPath;
    }

}
