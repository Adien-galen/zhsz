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
 * 创 建日期 ： 2016/6/22  9:14
 * <p/>
 * 描     述 ：dp和px的转换
 */
public class DensityUtils {

    public static int dp2px(Context ctx , float dp){
        //获取密度
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5f);

    }

    public static float px2dp(Context ctx,int px){
        float density = ctx.getResources().getDisplayMetrics().density;
        return px/density;
    }
}
