package com.example.zhsz.base;

import android.app.Activity;
import android.view.View;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/13  7:31
 * <p/>
 * 描     述 ：    菜单详情页基类
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;

    public View mRootView;  //根布局对象

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initViews();
    }

    /**
     * 初始化界面
     */
    public abstract View initViews();

    //初始化数据
    public void initData(){

    }
}
