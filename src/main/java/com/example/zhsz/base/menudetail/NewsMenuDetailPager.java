package com.example.zhsz.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.*;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhsz.R;
import com.example.zhsz.activity.MainActivity;
import com.example.zhsz.base.BaseMenuDetailPager;
import com.example.zhsz.base.TabDetailPager;
import com.example.zhsz.domain.NewsData;
import com.example.zhsz.domain.NewsData.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

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
 * 描     述 ：菜单详情页-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{

    private ViewPager mViewPager;
    private ArrayList<TabDetailPager> mPagerList;
    private ArrayList<NewsTabData> mNewsTabData; // 页签网络数据
    private TabPageIndicator mindicator;

    public NewsMenuDetailPager(Activity activity,ArrayList<NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_detail,null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
        ViewUtils.inject(this, view);
        mindicator = (TabPageIndicator)view.findViewById(R.id.indicator);

        // mViewPager.setOnPageChangeListener(this);//注意:当viewpager和Indicator绑定时,
        // 滑动监听需要设置给Indicator而不是viewpager
        mindicator.setOnPageChangeListener(this);
        return view;
    }

    @Override
    public void initData() {
        mPagerList = new ArrayList<TabDetailPager>();

        // 初始化页签数据
        for(int i=0;i<mNewsTabData.size();i++){
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mPagerList.add(pager);

        }

        mViewPager.setAdapter(new MenuDetailAdapter());
        mindicator.setViewPager(mViewPager);// 将viewpager和mIndicator关联起来,必须在viewpager设置完adapter后才能调用
    }

    // 跳转下一个页面
    @OnClick(R.id.im_next)
    public void nextPage(View view){
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);
    }



    class MenuDetailAdapter extends PagerAdapter {

        //重写此方法,返回页面标题,用于viewpagerIndicator的页签显示
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("onPageSelected:"+position);
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if(position==0){    //只有在第一个页面(北京), 侧边栏才允许出来
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
