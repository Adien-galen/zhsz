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
 * 创 建日期 ： 2016/6/15  6:56
 * <p/>
 * 描     述 ：页签详情页数据
 */
public class TabData {
    public int recode;
    public TabDetail data;

    public class TabDetail {
        public String title;
        public String more;
        public ArrayList<TabNewsData> news;
        public ArrayList<TopNewsData> topnews;

        @Override
        public String toString() {
            return "TabDetail{" +
                    "title='" + title + '\'' +
                    ", news=" + news +
                    ", topNews=" + topnews +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabData{" +
                "data=" + data +
                '}';
    }

    /**
     * 新闻列表对象
     */
    public class TabNewsData {
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }



    /**
     * 头条新闻
     */
    public class TopNewsData {
        public String id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TopNewsData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
