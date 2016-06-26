package com.example.zhsz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhsz.R;
import com.example.zhsz.activity.MainActivity;
import com.example.zhsz.base.BasePager;
import com.example.zhsz.base.impl.NewsCenterPager;
import com.example.zhsz.domain.NewsData;
import com.example.zhsz.domain.NewsData.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
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
 * 创 建日期 ： 2016/6/7  19:13
 * <p/>
 * 描     述 ：侧边栏
 */
public class LeftMenuFragment extends BaseFragment {
    @InjectView(R.id.lv_list)
    ListView lvList;

    private ArrayList<NewsMenuData> mMenuList;
    private int mCurrentPos;    // 当前被点击的菜单项
    private MenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();

                setCurrentMenuDetailPager(position);

                toggleSlidingMenu();// 隐藏
            }
        });
    }

    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUI = (MainActivity) mActivity;
        //获取主页面fragment
        ContentFragment contentFragment = mainUI.getContentFragment();
        // 获取新闻中心页面
        NewsCenterPager pager = contentFragment.getNewsCenterPager();
        // 设置当前菜单详情页
        pager.setCurrentMenuDetailPager(position);
    }

    /**
     * 切换SlidingMenu的状态
     */
    public void toggleSlidingMenu(){
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 切换状态, 显示时隐藏, 隐藏时显示
    }

    // 设置网络数据
    public void setMenuData(NewsData data){
        mMenuList = data.data;
        mAdapter =new MenuAdapter();
        lvList.setAdapter(mAdapter);
    }


    /**
     * 侧边栏数据适配器
     *
     */

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsMenuData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.list_menu_item,null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_menu_item_title);
            NewsMenuData newsMenuData = mMenuList.get(position);
            tvTitle.setText(newsMenuData.title);

            // 判断当前绘制的view是否被选中
            if(mCurrentPos==position){
                // 显示红色
                tvTitle.setEnabled(true);
            }else {
                // 显示白色
                tvTitle.setEnabled(false);
            }
            return view;
        }
    }
}
