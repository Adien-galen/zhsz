package com.example.zhsz.base.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhsz.R;
import com.example.zhsz.base.BaseMenuDetailPager;
import com.example.zhsz.domain.PhotoData;
import com.example.zhsz.global.GlobalContants;
import com.example.zhsz.utils.CacheUtils;
import com.example.zhsz.utils.bitmap.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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
 * 创 建日期 ： 2016/6/13  7:35
 * <p/>
 * 描     述 ：菜单详情页-组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    @InjectView(R.id.lv_photo)
    ListView lvPhoto;
    @InjectView(R.id.gv_photo)
    GridView gvPhoto;
//    private ListView lvPhoto;
//    private GridView gvPhoto;

    private ImageButton mBtnPhoto;

    private String mPhotoUrl;
    private ArrayList<PhotoData.PhotoInfo> mPhotoList;

    public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.mBtnPhoto = btnPhoto;

        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
            }
        });
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        ButterKnife.inject(this,view);
//        lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
//        gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
//        mBtnPhoto.setVisibility(View.VISIBLE);
        return view;
    }

    /**
     * 切换展现方式
     */
    private boolean isListDisplay = true;

    private void changeDisplay() {

        if (isListDisplay) {
            isListDisplay = false;
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);
            mBtnPhoto.setImageResource(R.mipmap.icon_pic_list_type);
        } else {
            isListDisplay = true;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            mBtnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);
        }

    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mPhotoUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        mPhotoUrl = GlobalContants.PHOTOS_URL;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, mPhotoUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;

                //设置缓存
                CacheUtils.setCache(mPhotoUrl, result, mActivity);
                System.out.println("photo页签详情页返回结果:" + result);
                parseData(result);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, "photo:" + msg, Toast.LENGTH_SHORT).show();
                System.out.println("访问失败" + msg);
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        PhotoData photoData = gson.fromJson(result, PhotoData.class);
        // 获取组图列表集合
        mPhotoList = photoData.data.news;
        if (mPhotoList != null) {
            PhotoAdapter adapter = new PhotoAdapter();
            lvPhoto.setAdapter(adapter);
            gvPhoto.setAdapter(adapter);
        }

    }

    class PhotoAdapter extends BaseAdapter {
        private MyBitmapUtils utils;

        PhotoAdapter() {
//            bitmapUtils = new BitmapUtils(mActivity);
            utils = new MyBitmapUtils();
        }


        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoData.PhotoInfo item = mPhotoList.get(position);
            holder.tvTitle.setText(item.title);
            utils.display(holder.ivPic, item.listimage);

            return convertView;
        }


    }

    static class ViewHolder {
        @InjectView(R.id.iv_pic)
        ImageView ivPic;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
