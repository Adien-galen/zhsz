package com.example.zhsz.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.zhsz.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class NewsDetailActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.btn_back)
    ImageButton btnBack;
    @InjectView(R.id.btn_share)
    ImageButton btnShare;
    @InjectView(R.id.btn_textsize)
    ImageButton btnTextsize;
    @InjectView(R.id.wb_web)
    WebView wbWeb;
    @InjectView(R.id.pb_progress)
    ProgressBar pbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);

        String url = getIntent().getStringExtra("url");
        System.out.println("webView:" + url);
        WebSettings settings = wbWeb.getSettings();
        settings.setJavaScriptEnabled(true); //设置网页支持js
        settings.setBuiltInZoomControls(true); //设置网页支持缩放
        settings.setUseWideViewPort(true);//支持双击缩放

        wbWeb.setWebViewClient(new WebViewClient() {
            /**
             * 网页开始加载
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载");
                pbProgress.setVisibility(View.VISIBLE);
            }

            /**
             * 网页加载结束
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页开始结束");
                pbProgress.setVisibility(View.GONE);
            }

            /**
             * 所有跳转的链接都会在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转URL:" + url);
                view.loadUrl(url);
                return true;
            }
        });

        wbWeb.setWebChromeClient(new WebChromeClient() {

            /**
             * 进度发生变化
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("加载进度：" + newProgress);
            }

            /**
             * 获取网页标题
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("网页标题：" + title);
            }
        });

        // 加载网页
//        wbWeb.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseTextDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
            default:
                break;
        }
    }


    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


    private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
    private int mCurrentItem = 2;//记录当前的item,

    /**
     * 弹出选择字体对话框
     */
    private void showChooseTextDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体"};

        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wbWeb.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }

                mCurrentItem = mCurrentChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
