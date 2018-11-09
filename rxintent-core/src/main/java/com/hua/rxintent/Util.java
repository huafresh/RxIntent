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
class Util {

    static File createFile(Context context, String dirPath, String name) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        File file = new File(dirPath + File.separator + name);
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
        return file;
    }

    static String getCacheDirPath(Context context) {
        String path;
        if (Util.isExternalExist()) {
            path = context.getExternalCacheDir().getAbsolutePath();
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }

    static Uri getUriFromFile(Context context, File file) {
        ApplicationInfo info = context.getApplicationInfo();
        if (isN() &&
                info.targetSdkVersion >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    info.packageName + ".rxintent.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 是否是7.0
     */
    static boolean isN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    static boolean isExternalExist() {
        return MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
