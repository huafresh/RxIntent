package com.hua.rxintent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author hua
 * @version 2018/11/7 17:29
 */

class CropIntent extends AbstractIntent<Intent, String> {

    /**
     * 裁剪图片默认的宽和高
     */
    private static final int DEFAULT_WIDTH = 150;
    private static final int DEFAULT_HEIGHT = 150;
    /**
     * 相册选择后原始图片存储的名称
     */
    private static final String TEMP_FILE_NAME = "crop_temp_file.jpg";
    private static String savePath;
    private String originPath;

    CropIntent(String originPath) {
        this.originPath = originPath;
    }

    @Override
    public Intent build(Context context) {
        String cacheDirPath = Util.getCacheDirPath(context);
        File saveFile = Util.createFile(context, cacheDirPath, TEMP_FILE_NAME);
        if (saveFile != null) {
            Uri orgUri = Uri.fromFile(new File(originPath));
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(orgUri, "image/*");
            //在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            //aspectX aspectY是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", DEFAULT_WIDTH);
            intent.putExtra("outputY", DEFAULT_HEIGHT);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getUriFromFile(context, saveFile));
            savePath = saveFile.getAbsolutePath();
            //是否将数据保留在Bitmap中返回
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            return intent;
        }
        return null;
    }


    @Override
    public String handle(Intent result) {
        return result != null ? savePath : null;
    }
}
