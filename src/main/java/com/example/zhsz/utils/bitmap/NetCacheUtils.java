package com.example.zhsz.utils.bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/21  5:59
 * <p/>
 * 描     述 ：网络缓存
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils,MemoryCacheUtils memoryCacheUtils){
        mLocalCacheUtils=localCacheUtils;
        mMemoryCacheUtils=memoryCacheUtils;
    }

    /**
     * 从网络下载图片
     *
     * @param ivPic
     * @param url
     */
    public void getBitmapFromNet(ImageView ivPic ,String url){
        new BitmapTask().execute(ivPic,url);// 启动AsyncTask,参数会在doInbackground中获取
    }


    /**
     * Handler和线程池的封装
     *
     * 第一个泛型: 参数类型 第二个泛型: 更新进度的泛型, 第三个泛型是onPostExecute的返回结果
     *
     * @author Kevin
     *
     */
    class BitmapTask extends AsyncTask<Object,Void,Bitmap>{
        private ImageView ivPic;
        private String url;

        @Override
        protected Bitmap doInBackground(Object[] params) { //后台耗时方法在此执行, 子线程
            ivPic= (ImageView) params[0];
            url= (String) params[1];

//            ivPic.setTag(url); //将url和图片绑定
            return downLoadBitmap(url);
        }

        //更新进度, 主线程
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //耗时方法结束后,执行该方法, 主线程

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null){
//                String bindUrl = (String) ivPic.getTag();
//                if(url.equals(bindUrl)){    // 确保图片设定给了正确的imageview
                    ivPic.setImageBitmap(result);
                    mLocalCacheUtils.setBitmapToLocal(url,result);// 将图片保存在本地
                    mMemoryCacheUtils.setBitmaptoMemory(url,result); // 将图片保存在内存
                    System.out.println("从网络缓存读取图片啦...");
//                }
            }
        }
    }


    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    private Bitmap downLoadBitmap(String url) {
        HttpURLConnection conn=null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();

            int code = conn.getResponseCode();
            if(code==200){
                InputStream inputStream = conn.getInputStream();
                //图片压缩处理
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//宽高都压缩为原来的二分之一, 此参数需要根据图片要展示的大小来确定
                options.inPreferredConfig = Bitmap.Config.RGB_565;//设置图片格式

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
