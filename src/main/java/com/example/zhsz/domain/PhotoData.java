package com.example.zhsz.domain;

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
 * 创 建日期 ： 2016/6/19  19:38
 * <p/>
 * 描     述 ：组图数据
 */
public class PhotoData {
    public int retcode;
    public PhotosInfo data;
    public class PhotosInfo{
        public String title;
        public ArrayList<PhotoInfo> news;
    }

    public class PhotoInfo{
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
