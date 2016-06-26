package com.example.zhsz.activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.zhsz.R;
import com.example.zhsz.fragment.ContentFragment;
import com.example.zhsz.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 设置侧边栏
        setBehindContentView(R.layout.left_item);
        // 获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        // 设置全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置预留屏幕的宽度
//        slidingMenu.setBehindOffset(200);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset((int) (0.65*width));
        initFragment();
    }

    /**
     * 初始化fragment, 将fragment数据填充给布局文件
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 用fragment替换framelayout
        transaction.replace(R.id.fl_content,new ContentFragment(),FRAGMENT_CONTENT);
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
        // 提交事务
        transaction.commit();

    }

    // 获取侧边栏fragment
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment)fm.findFragmentByTag(FRAGMENT_LEFT_MENU);

        return fragment;
    }

    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment)fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }

}
