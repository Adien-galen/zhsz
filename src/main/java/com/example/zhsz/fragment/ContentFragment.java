package com.example.zhsz.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.zhsz.R;
import com.example.zhsz.base.BasePager;
import com.example.zhsz.base.impl.GovAffairsPager;
import com.example.zhsz.base.impl.HomePager;
import com.example.zhsz.base.impl.NewsCenterPager;
import com.example.zhsz.base.impl.SettingPager;
import com.example.zhsz.base.impl.SmartServicePager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * 创 建日期 ： 2016/6/7  19:13
 * <p/>
 * 描     述 ：主页内容
 */
public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.rg_group)
    private RadioGroup rgGroup;

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mPagerList;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this, view); // 注入view和事件
        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home); //默认勾选首页

        // 初始化5个子页面
        mPagerList = new ArrayList<BasePager>();

        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartServicePager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        // 监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        //设置当前页面并去掉切换页面的动画
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gav:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // 获取当前被选中的页面, 初始化该页面数据
                mPagerList.get(i).initData();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 初始化首页数据
        mPagerList.get(0).initData();

    }

    private class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view ==o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     *
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPagerList.get(1);
    }


}
