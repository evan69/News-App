package com.ihandy.a2014011433;

/**
 * Created by Evan Hou on 2016/9/3.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ihandy.a2014011433.news.News;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
//import org.apache.*;

public class NewsActivity extends Activity implements View.OnClickListener {
    //private RelativeLayout mLayoutTitleBar;
    private Button mBackwardbButton;
    private Button mForwardButton;
    private Button mShareButton;
    private ImageButton mFavoriteButton;
    private FrameLayout mContentLayout;
    private WebView mWebView;

    //private ViewGroup menuView;
    private PopupWindow mPopWindow;
    //add for share

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();   //加载 activity_title 布局 ，并获取标题及两侧按钮

        Button shareButton = (Button)findViewById(R.id.share_button);
    }


    private void setupViews() {
        super.setContentView(R.layout.activity_news);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content_news);
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mFavoriteButton = (ImageButton) findViewById(R.id.favorite_button);
        mShareButton = (Button) findViewById(R.id.share_button);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        mWebView = (WebView)findViewById(R.id.news_view);
        List<News> arr = MainActivity.getDataBase().findSelected(Long.parseLong(MainActivity.currentNews.news_id));
        if(arr.size() < 1)
        {
            Log.d("database","not favorite");
            mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.grey_favorite));
            //FavoriteActivity.update();
        }
        else
        {
            Log.d("database","is favorite");
            mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.red_heart));
        }
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                Log.d("page","start");
                //mWebView.getSettings().setBlockNetworkImage(true);
                //mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url)
            {
                Log.d("page","finish");
                //mWebView.getSettings().setBlockNetworkImage(false);
                //mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                super.onPageFinished(view, url);
            }
        });
        Log.d("url",getIntent().getStringExtra("url"));
        mWebView.loadUrl(getIntent().getStringExtra("url"));
        //mWebView.loadUrl("https://www.baidu.com");
        //mWebView.loadUrl("http://www.indiatimes.com/news/india/mumbai-father-couldn-t-save-his-depressed-son-after-being-denied-half-day-leave-from-govt-job-260307.html");
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(NewsActivity.this).inflate(R.layout.popuplayout, null);
        mPopWindow = new PopupWindow(contentView,
                GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        View rootview = LayoutInflater.from(NewsActivity.this).inflate(R.layout.activity_news, null);
        mPopWindow.setBackgroundDrawable(new ColorDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    protected void showBackwardView(int backwardResid, boolean show) {
        if (mBackwardbButton != null) {
            if (show) {
                mBackwardbButton.setText(backwardResid);
                mBackwardbButton.setVisibility(View.VISIBLE);
            } else {
                mBackwardbButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    protected void showForwardView(int forwardResId, boolean show) {
        if (mForwardButton != null) {
            if (show) {
                mForwardButton.setVisibility(View.VISIBLE);
                mForwardButton.setText(forwardResId);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    protected void onBackward(View backwardView) {
        Toast.makeText(this, "return to main ", Toast.LENGTH_LONG).show();
        finish();
    }

    //取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_button:
                onBackward(v);
                break;

            case R.id.favorite_button:
                List<News> arr = MainActivity.getDataBase().findSelected(Long.parseLong(MainActivity.currentNews.news_id));
                if(arr.size() < 1)
                {
                    Log.d("database","insert");
                    MainActivity.getDataBase().insertDetailNews(MainActivity.currentNews);
                    mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.red_heart));
                    //FavoriteActivity.update();
                }
                else
                {
                    Log.d("database","del");
                    MainActivity.getDataBase().del(Long.parseLong(MainActivity.currentNews.news_id));
                    mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.grey_favorite));
                    //FavoriteActivity.update();
                }
                break;

            case R.id.share_button:
                shareMsg("share this news to",MainActivity.currentNews.title,MainActivity.currentNews.source_url,null);
                //showPopupWindow();
                break;
            default:
                break;
        }
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
}

