package com.example.zhsz.utils;

import android.content.Context;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/19  18:15
 * <p/>
 * 描     述 ：缓存工具类,key是url，value是json
 */
public class CacheUtils {


    /**
     * 设置缓存 key 是url, value是json
     */
    public static void setCache(String key, String value, Context ctx){
        PrefUtils.setString(ctx,key,value);
    }

    /**
     * 获取缓存 key 是url
     */
    public static String getCache(String key,Context ctx){
       return PrefUtils.getString(ctx,key,null);
    }
}
