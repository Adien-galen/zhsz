package com.example.zhsz.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.zhsz.utils.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/21  6:14
 * <p/>
 * 描     述 ：本地缓存
 */
public class LocalCacheUtils {
    public static final String CACHE_PATH = Environment.getDownloadCacheDirectory().getAbsolutePath() + "/zhsz_cache";

    /**
     * 从本地sdcard读图片
     */
    public Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 向sdcard写图片
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH, fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }

            //将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
