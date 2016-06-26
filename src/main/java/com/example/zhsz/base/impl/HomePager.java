package com.example.zhsz.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhsz.base.BasePager;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/12  20:00
 * <p/>
 * 描     述 ：
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("初始化首页数据....");
        tvTitle.setText("智慧深圳");    // 修改标题
        btnMenu.setVisibility(View.GONE);   // 隐藏菜单按钮
        setSlidingMenuEnable(false);    //关闭侧边栏

        TextView text = new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        // 向FrameLayout中动态添加布局
        flContent.addView(text);
    }



}
