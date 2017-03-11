package com.ihandy.a2014011433.myadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihandy.a2014011433.MainActivity;
import com.ihandy.a2014011433.NewsActivity;
import com.ihandy.a2014011433.R;
import com.ihandy.a2014011433.news.News;
//import com.ihandy.a2014011433.news.NewsDetail;
import com.ihandy.a2014011433.news.News;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.ihandy.a2014011433.views.WrapContentDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Evan Hou on 2016/9/1.
 */
public class NewsRefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    public final static int TYPE_FOOTER = 2;
    private List<News> articleList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    public NewsRefreshAdapter(Context context, List<News> articleList) {
        this.context = context;
        if (articleList == null) {
            this.articleList = new ArrayList<News>();
        } else {
            this.articleList = articleList;
        }
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        News article = articleList.get(position);
        //  footer
        if (article == null)
            return TYPE_FOOTER;
        //  normal
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    public long getBottomArticleId() {
        if (articleList == null || articleList.size() == 0 || articleList.get(articleList.size() - 1) == null)
            return -1;
        return Long.parseLong(articleList.get(articleList.size() - 1).news_id);
    }

    public long getTopArticleId() {
        if (articleList == null || articleList.size() == 0 || articleList.get(articleList.size() - 1) == null)
            return -1;
        return Long.parseLong((articleList.get(0)).news_id);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER: {
                view = mLayoutInflater.inflate(
                        R.layout.my_news_item_small, parent, false);
                return new NewsItemViewHolder(view);
            }
            case TYPE_CELL: {
                view = mLayoutInflater.inflate(
                        R.layout.my_news_item_small, parent, false);
                return new NewsItemViewHolder(view);
            }
            case TYPE_FOOTER:
                view = mLayoutInflater.inflate(
                        R.layout.recyclerview_footer, parent, false);
                return new FooterViewHolder(view);
        }
        Log.e("refresh", "holder bug!!!");
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).rcvLoadMore.spin();
            return;
        }
        NewsItemViewHolder newHolder = (NewsItemViewHolder)holder;
        final News article = articleList.get(position);
        newHolder.newsArticlePhoto.setImageURI(Uri.parse(article.imgURL));
        newHolder.newsArticleTitle.setText(article.title);
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd HH:mm");
        Date date = new Date(Long.parseLong(article.fetched_time));
        newHolder.newsArticleDate.setText(sdf.format(date));
        newHolder.newsArticleSource.setText(article.category);
        newHolder.newsCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                MainActivity m = (MainActivity)context;
                MainActivity.currentNews = article;
                Intent intent = new Intent(m,NewsActivity.class);
                intent.putExtra("url",article.source_url);
                // 启动指定Activity并等待返回的结果，其中0是请求码，用于标识该请求
                m.startActivityForResult(intent, 0);
                //new FinestWebView.Builder((MainActivity)context).menuTextFont("haha").show(article.url);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (articleList != null) {
            return articleList.size();
        } else {
            return 0;
        }
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_article_photo)
        WrapContentDraweeView newsArticlePhoto;
        @BindView(R.id.news_article_title)
        TextView newsArticleTitle;
        @BindView(R.id.news_article_source)
        TextView newsArticleSource;
        @BindView(R.id.news_article_date)
        TextView newsArticleDate;
        @BindView(R.id.cv_item)
        CardView newsCardView;
        public NewsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rcv_load_more)
        ProgressWheel rcvLoadMore;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}