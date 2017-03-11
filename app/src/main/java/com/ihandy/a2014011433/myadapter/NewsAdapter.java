package com.ihandy.a2014011433.myadapter;

/**
 * Created by Evan Hou on 2016/9/2.
 */

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
import com.ihandy.a2014011433.views.WrapContentDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageItemArticleViewHolder> {

    //  头条新闻size大一点
    static final int TYPE_HEADER = 0;
    //  第二条新闻size小一点
    static final int TYPE_CELL = 1;

    //新闻列表
    private List<News> articleList;

    //context
    private Context context;

    private LayoutInflater mLayoutInflater;


    public NewsAdapter(Context context, List<News> articleList) {
        this.context = context;
        this.articleList = articleList;
        mLayoutInflater = LayoutInflater.from(context);
    }


    //根据位置判断是否是头条新闻
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }


    @Override
    public NewsAdapter.ImageItemArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                Log.e("mabi", "wocao");
                view = mLayoutInflater.inflate(
                        R.layout.my_news_item_big, parent, false);
                break;
            }
            case TYPE_CELL: {
                view = mLayoutInflater.inflate(
                        R.layout.my_news_item_small, parent, false);
                break;
            }
        }
        view = mLayoutInflater.inflate(
                R.layout.my_news_item_small, parent, false);
        return new ImageItemArticleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ImageItemArticleViewHolder holder, int position) {

        final News article = articleList.get(position);
        //holder.newsArticlePhoto.setAspectRatio(2.0F);
        //holder.newsArticlePhoto.
        holder.newsArticlePhoto.setImageURI(Uri.parse(article.imgURL));
        //holder.newsArticlePhoto.set
        holder.newsArticleTitle.setText(article.title);
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd HH:mm");
        Date date = new Date(Long.parseLong(article.fetched_time));
        holder.newsArticleDate.setText(sdf.format(date));
        holder.newsArticleSource.setText(article.category);
        holder.newsCardView.setOnClickListener(new View.OnClickListener(){

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
        return articleList.size();
    }

    class ImageItemArticleViewHolder extends RecyclerView.ViewHolder {

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
        public ImageItemArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}