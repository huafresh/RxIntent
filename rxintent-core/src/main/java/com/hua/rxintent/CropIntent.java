package com.hua.rxintent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

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
    private Uri originUri;
    private static final String FORMAT_MSG = "无法对%s文件生成content://形式的Uri，请使用重载方法openCrop(final FragmentActivity activity, Uri uri)调起应用裁剪。";

    CropIntent(String originPath) {
        this.originPath = originPath;
        this.originUri = null;
    }

    CropIntent(Uri originUri) {
        this.originUri = originUri;
        this.originPath = null;
    }

    private static void checkUri(Context context, Uri uri) {
        if (Util.isN() &&
                Util.isTargetN(context) &&
                !"content".equals(uri.getScheme())) {
            throw new IllegalArgumentException("7.0以上时，调起裁剪的Uri的scheme不可以是\"file\"");
        }
    }

    @Override
    public Intent build(Context context) {
        String cacheDirPath = Util.getCacheDirPath(context);
        File saveFile = Util.createFileIfNeed(context, cacheDirPath, TEMP_FILE_NAME);
        if (saveFile != null) {
            Uri orgUri = originUri;
            if (orgUri == null) {
                try {
                    orgUri = Util.getCompatUriFromFile(context, new File(originPath));
                } catch (Exception e) {
                    throw new UriGrantedException(String.format(FORMAT_MSG, originPath), e);
                }
            }
            checkUri(context, orgUri);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
            if (Util.isN()) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
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
    String[] needPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            return new String[0];
        }
    }


    @Override
    public String convert(Intent intent) {
        return intent != null ? savePath : null;
    }
}
