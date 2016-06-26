package com.example.zhsz.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhsz.R;
import com.example.zhsz.activity.NewsDetailActivity;
import com.example.zhsz.domain.NewsData.NewsTabData;
import com.example.zhsz.domain.TabData;
import com.example.zhsz.domain.TabData.*;
import com.example.zhsz.global.GlobalContants;
import com.example.zhsz.utils.CacheUtils;
import com.example.zhsz.utils.PrefUtils;
import com.example.zhsz.view.RefreshListView;
import com.example.zhsz.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

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
 * 创 建日期 ： 2016/6/13  7:45
 * <p/>
 * 描     述 ：
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    NewsTabData mTabData;

    @ViewInject(R.id.lv_list)
    RefreshListView lvList; // 新闻列表
    @ViewInject(R.id.vp_news)
    TopNewsViewPager vpNews;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;   //头条新闻的标题
    @ViewInject(R.id.indicator)
    CirclePageIndicator indicator; // 头条新闻位置指示器

    private String mUrl;
    private TabData mTabDetailData;
    private ArrayList<TabData.TopNewsData> mTopNewsList; // 头条新闻数据集合
    private ArrayList<TabData.TabNewsData> mNewsList;    // 新闻数据集合
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;// 更多页面的地址
    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);

        lvList.addHeaderView(headerView);// 将头条新闻以头布局的形式加给listview

        // 设置下拉刷新监听
        lvList.setOnrefreshListener(new RefreshListView.OnrefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void loadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String readId = mNewsList.get(position).id;
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity, "read_ids", ids);

                }
                changeReadState(view);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }


    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache, false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("返回结果：" + result);
                parseData(result, false);

                //设置缓存
                CacheUtils.setCache(mUrl, result, mActivity);

                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "访问失败" + msg, Toast.LENGTH_SHORT).show();
                System.out.println("访问失败" + msg);
                error.printStackTrace();
                lvList.onRefreshComplete(false);
            }
        });
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("页签详情页返回结果:" + result);
                parseData(result, true);

                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "下一页访问失败" + msg, Toast.LENGTH_SHORT).show();
                System.out.println("访问失败" + msg);
                error.printStackTrace();
                lvList.onRefreshComplete(false);
            }
        });
    }


    protected void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签详情解析：" + mTabDetailData);

        // 处理下一页链接
        String more = mTabDetailData.data.more;
        if (TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URL + more;
        } else {
            mMoreUrl = null;
        }

        if (!isMore) {
            // 处理下一页链接
            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;
            if (mTopNewsList != null) {
                vpNews.setAdapter(new TopNewsAdapter());
                System.out.println("让指示器重新定位到第一个点");
                indicator.setViewPager(vpNews);
                indicator.setSnap(true);//支持快照
                indicator.setOnPageChangeListener(this);
                // 让指示器重新定位到第一个点
                indicator.onPageSelected(0);
                tvTitle.setText(mTopNewsList.get(0).title);
            }

            if (mNewsList != null) {

                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }

            // 自动轮播条显示
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vpNews.getCurrentItem();
                        if (currentItem < mTopNewsList.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }
                        vpNews.setCurrentItem(currentItem);// 切换到下一个页面
                        mHandler.sendEmptyMessageDelayed(0, 2000);// 继续延时2秒发消息,形成循环
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 2000);// 延时2秒后发消息
            }

        } else {// 如果是加载下一页,需要将数据追加给原来的集合
            ArrayList<TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();
        }

    }


    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {
        private BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.topnews_item_default);// 设置默认图片
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            // 基于控件大小填充图片
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            // 传递imagView对象和图片地址
            utils.display(image, topNewsData.topimage);
            container.addView(image);

            System.out.println("instantiateItem:" + position);
            image.setOnTouchListener(new TopNewsTouchListener());//设置触摸监听
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 头条新闻的触摸监听
     */
    class TopNewsTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    System.out.println("按下");
                    mHandler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
                    break;
                case MotionEvent.ACTION_UP:
                    System.out.println("抬起");
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    System.out.println("取消");
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                    break;

                default:
                    break;
            }
            return true;
        }
    }


    /**
     * 新闻列表适配器
     */
    private class NewsAdapter extends BaseAdapter {
        private BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }


        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TabNewsData item = (TabNewsData) getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);
            utils.display(holder.ivPic, item.listimage);

            return convertView;
        }

    }

    static class ViewHolder {
        @InjectView(R.id.iv_pic)
        ImageView ivPic;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_date)
        TextView tvDate;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tvTitle.setText(mTopNewsList.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
