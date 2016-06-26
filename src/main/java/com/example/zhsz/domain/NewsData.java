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
 * 创 建日期 ： 2016/6/12  21:52
 * <p/>
 * 描     述 ：网络分类信息的封装,字段名字必须和服务器返回的字段名一致, 方便gson解析
 */
public class NewsData {
    public int retcode;
    public ArrayList<NewsMenuData> data;

    // 侧边栏数据对象
    public class NewsMenuData{
        public String id;
        public String title;
        public int type;
        public String url;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }

    }

    // 新闻页面下11个子页签的数据对象
    public class NewsTabData {
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
