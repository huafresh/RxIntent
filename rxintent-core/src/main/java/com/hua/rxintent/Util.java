package com.hua.rxintent;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import android.util.Log;

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

    static boolean debugEnable = false;

    static File createFileIfNeed(Context context, String dirPath, String name) {
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

    static Uri getCompatUriFromFile(Context context, File file) {
        ApplicationInfo info = context.getApplicationInfo();
        if (isN() && isTargetN(context)) {
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

    static boolean isTargetN(Context context){
        ApplicationInfo info = context.getApplicationInfo();
        return info.targetSdkVersion >= Build.VERSION_CODES.N;
    }

    static boolean isExternalExist() {
        return MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    static void d(String msg) {
        if (debugEnable) {
            Log.d("@@@RxIntent", msg);
        }
    }

    static void e(String msg) {
        if (debugEnable) {
            Log.e("@@@RxIntent", msg);
        }
    }
}
