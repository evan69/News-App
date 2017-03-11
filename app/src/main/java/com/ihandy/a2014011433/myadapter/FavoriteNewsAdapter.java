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

import com.ihandy.a2014011433.FavoriteActivity;
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

public class FavoriteNewsAdapter extends RecyclerView.Adapter<FavoriteNewsAdapter.ImageItemArticleViewHolder> {
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private List<News> articleList;
    private Context context;
    private LayoutInflater mLayoutInflater;
    public FavoriteNewsAdapter(Context context, List<News> articleList) {
        this.context = context;
        this.articleList = articleList;
        mLayoutInflater = LayoutInflater.from(context);
    }
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
    public FavoriteNewsAdapter.ImageItemArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = mLayoutInflater.inflate(
                R.layout.my_news_item_small, parent, false);
        return new ImageItemArticleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ImageItemArticleViewHolder holder, int position) {

        final News article = articleList.get(position);
        holder.newsArticlePhoto.setImageURI(Uri.parse(article.imgURL));
        holder.newsArticleTitle.setText(article.title);
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd HH:mm");
        Date date = new Date(Long.parseLong(article.fetched_time));
        holder.newsArticleDate.setText(sdf.format(date));
        holder.newsArticleSource.setText(article.category);
        holder.newsCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FavoriteActivity m = (FavoriteActivity)context;
                MainActivity.currentNews = article;
                Intent intent = new Intent(m,NewsActivity.class);
                intent.putExtra("url",article.source_url);
                m.startActivityForResult(intent, 0);
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