package com.example.zhsz.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/15  6:00
 * <p/>
 * 描     述 ：11个子页签水平滑动的Viewpager
 */
public class HorizontalViewPager extends ViewPager {

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalViewPager(Context context) {
        super(context);
    }

    /**
     * 事件分发, 请求父控件及祖宗控件是否拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);
        }else {// 如果是第一个页面,需要显示侧边栏, 请求父控件拦截
            getParent().requestDisallowInterceptTouchEvent(false);// 拦截
        }
        return super.dispatchTouchEvent(ev);
    }
}
