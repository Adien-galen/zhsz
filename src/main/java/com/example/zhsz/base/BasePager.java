package com.example.zhsz.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhsz.R;
import com.example.zhsz.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;

import butterknife.InjectView;

/**
 * ============================================================
 * <p/>
 * 版     权 ：  2016
 * <p/>
 * 作     者  :  崔桂林
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2016/6/12  19:42
 * <p/>
 * 描     述 ：主页下5个子页面的基类
 */
public class BasePager {

    public Activity mActivity;
    public View mRootView;// 布局对象

    public TextView tvTitle;// 标题对象

    public FrameLayout flContent;// 内容

    public ImageButton btnMenu;// 菜单按钮
    public ImageButton btnPhoto;


    public BasePager(Activity activity) {
        mActivity = activity;
        initViews();
    }

    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 切换SlidingMenu的状态
     *
     */
    public void toggleSlidingMenu() {
        MainActivity mianUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mianUI.getSlidingMenu();
        slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }

    /**
     * 设置侧边栏开启或关闭
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
