package com.example.zhsz.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhsz.activity.MainActivity;
import com.example.zhsz.base.BaseMenuDetailPager;
import com.example.zhsz.base.BasePager;
import com.example.zhsz.base.menudetail.InteractMenuDetailPager;
import com.example.zhsz.base.menudetail.NewsMenuDetailPager;
import com.example.zhsz.base.menudetail.PhotoMenuDetailPager;
import com.example.zhsz.base.menudetail.TopicMenuDetailPager;
import com.example.zhsz.domain.NewsData;
import com.example.zhsz.fragment.LeftMenuFragment;
import com.example.zhsz.global.GlobalContants;
import com.example.zhsz.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.HttpUtils.*;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.*;

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
 * 创 建日期 ： 2016/6/12  20:00
 * <p/>
 * 描     述 ：新闻中心
 */
public class NewsCenterPager extends BasePager {

    private NewsData mNewsData;
    private ArrayList<BaseMenuDetailPager> mPagers; // 4个菜单详情页的集合
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("初始化新闻中心数据....");
        tvTitle.setText("新闻");    // 修改标题
        setSlidingMenuEnable(true);// 打开侧边栏

        String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL, mActivity);

        if(!TextUtils.isEmpty(cache)){// 如果缓存存在,直接解析数据, 无需访问网路
            parseData(cache);
        }
        getDataFromServer();// 不管有没有缓存, 都获取最新数据, 保证数据最新
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        // 使用xutils发送请求
        httpUtils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {

            // 访问成功
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果："+result);
                parseData(result);

                // 设置缓存
                CacheUtils.setCache(GlobalContants.CATEGORIES_URL,result,mActivity);

            }

            // 访问失败
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity,"访问失败"+msg,Toast.LENGTH_SHORT).show();
                System.out.println("访问失败"+msg);
                error.printStackTrace();

            }
        });

    }

    /**
     * 解析网络数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果："+mNewsData);

        //刷新侧边栏数据
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        // 准备4个菜单详情页
        mPagers = new ArrayList<BaseMenuDetailPager>();

        mPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        // 设置菜单详情页-新闻为默认当前页
        setCurrentMenuDetailPager(0);
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        // 获取当前要显示的菜单详情页
        BaseMenuDetailPager pager = mPagers.get(position);
        // 清除之前的布局
        flContent.removeAllViews();
        // 将菜单详情页的布局设置给帧布局
        flContent.addView(pager.mRootView);

        // 设置当前页的标题
        NewsData.NewsMenuData menuData = mNewsData.data.get(position);
        tvTitle.setText(menuData.title);

        // 初始化当前页面的数据
        pager.initData();
        if(pager instanceof PhotoMenuDetailPager){
            btnPhoto.setVisibility(View.VISIBLE);
        }else {
            btnPhoto.setVisibility(View.INVISIBLE);
        }
    }

}
