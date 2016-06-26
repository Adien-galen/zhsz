package com.example.zhsz.utils.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/21  6:30
 * <p/>
 * 描     述 ：内存缓存
 */
public class MemoryCacheUtils {

    private LruCache<String, Bitmap> mLruCache;

    public MemoryCacheUtils(){
        long maxMemory = Runtime.getRuntime().maxMemory()/8;//模拟器默认是16m
        // 获取图片占用内存大小
        mLruCache = new LruCache<String,Bitmap>((int) maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getRowBytes() * value.getHeight();// 获取图片占用内存大小
                return byteCount;
            }
        };
    }

    /**
     * 从内存读
     *
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url){
        return mLruCache.get(url);
    }

    /**
     * 写内存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmaptoMemory(String url,Bitmap bitmap){
        mLruCache.put(url,bitmap);
    }
}
