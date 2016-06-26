package com.example.zhsz.utils.bitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.zhsz.R;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/21  5:54
 * <p/>
 * 描     述 ：自定义图片加载工具
 */
public class MyBitmapUtils {
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils() {
        mLocalCacheUtils = new LocalCacheUtils();
        mMemoryCacheUtils = new MemoryCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }

    public void display(ImageView ivpic, String url){
        ivpic.setImageResource(R.mipmap.news_pic_default);// 设置默认加载图片

        Bitmap bitmap=null;
        //从内存读;
        bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
        if(bitmap!=null){
            ivpic.setImageBitmap(bitmap);
            System.out.println("从内存读取图片啦...");
            return;
        }

        //从本地读
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap!=null){
            ivpic.setImageBitmap(bitmap);
            System.out.println("从本地读取图片啦...");
            mMemoryCacheUtils.setBitmaptoMemory(url,bitmap);
            return;
        }

        //从网络读
        mNetCacheUtils.getBitmapFromNet(ivpic,url);

    }
}
