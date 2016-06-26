package com.example.zhsz.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhsz.base.BaseMenuDetailPager;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/13  7:35
 * <p/>
 * 描     述 ：菜单详情页-专题
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView text = new TextView(mActivity);
        text.setText("菜单详情页-专题");
        text.setTextColor(Color.RED);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);
        return text;
    }


}
