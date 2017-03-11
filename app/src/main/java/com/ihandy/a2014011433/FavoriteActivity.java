package com.ihandy.a2014011433;

/**
 * Created by Evan Hou on 2016/9/6.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihandy.a2014011433.R;
import com.ihandy.a2014011433.myadapter.FavoriteNewsAdapter;
import com.ihandy.a2014011433.myadapter.NewsAdapter;
import com.ihandy.a2014011433.mydb.DetailNewsFavorite;
import com.ihandy.a2014011433.mydb.MyFavoriteDb;
import com.ihandy.a2014011433.network.EngAllNewsParser;
import com.ihandy.a2014011433.news.News;
import com.ihandy.a2014011433.news.NewsFactory;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Evan Hou on 2016/9/3.
 */

public class FavoriteActivity extends Activity implements View.OnClickListener {
    private TextView mTitleTextView;
    private Button mBackwardbButton;
    private Button mForwardButton;

    RecyclerView newsArticle;
    private List<News> itemArticleList = new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();   //加载 activity_title 布局 ，并获取标题及两侧按钮
    }

    public void update()
    {
        DetailNewsFavorite data = MainActivity.getDataBase();
        MyFavoriteDb helper = data.helper;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("favorite",null,null,null,null,null,null);
        itemArticleList.clear();
        if(cursor.moveToFirst())
        {
            do {
                News tmp = new News();
                tmp.title = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnTitle()));
                tmp.imgURL = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnImaginurl()));
                tmp.news_id = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnNewsid()));
                tmp.category = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbOrigin()));
                tmp.fetched_time = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbTime()));
                tmp.source_url = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnUrl()));
                itemArticleList.add(tmp);
            }while(cursor.moveToNext());
            cursor.close();
        }
        FavoriteNewsAdapter adapter = new FavoriteNewsAdapter(this, itemArticleList);
        newsArticle.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        update();
    }

    private void setupViews() {
        super.setContentView(R.layout.activity_favorite);
        mTitleTextView = (TextView) findViewById(R.id.favorite_text_title);
        mBackwardbButton = (Button) findViewById(R.id.favorite_button_backward);
        //mForwardButton = (Button) findViewById(R.id.favorite_button_forward);
        newsArticle = (RecyclerView) findViewById(R.id.favorite_news_article);

        newsArticle.setLayoutManager(new LinearLayoutManager(this));
        update();
    }

    /**
     * 是否显示返回按钮
     * @param backwardResid  文字
     * @param show  true则显示
     */
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
        //Toast.makeText(this, "点击返回，可在此处调用finish()", Toast.LENGTH_LONG).show();
        finish();
    }

    protected void onForward(View forwardView) {
    }

    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setTextColor(textColor);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.favorite_button_backward:
                onBackward(v);
                break;

            default:
                break;
        }
    }
}

