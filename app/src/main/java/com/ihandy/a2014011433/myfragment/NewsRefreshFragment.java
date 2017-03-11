package com.ihandy.a2014011433.myfragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.ihandy.a2014011433.MainActivity;
import com.ihandy.a2014011433.R;
import com.ihandy.a2014011433.myadapter.NewsRefreshAdapter;
import com.ihandy.a2014011433.network.EngAllNewsParser;
import com.ihandy.a2014011433.news.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.ihandy.a2014011433.news.NewsFactory;
import com.nispok.snackbar.Snackbar;

/**
 * Created by Evan Hou on 2016/9/1.
 */
public class NewsRefreshFragment extends Fragment {
    private static final int VISIBLE_THRESHOLD = 3;
    Unbinder ButterKnifeReset;
    private static final String STORE_PARAM = "param";
    @BindView(R.id.rcv_article_origin)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    NewsRefreshAdapter mAdapter;
    private String mParam;
    private List<News> mArticleList = new ArrayList<News>();
    private Activity mActivity;

    private boolean loading = false;

    public static Fragment newInstance(String param) {
        NewsRefreshFragment fragment = new NewsRefreshFragment();
        Bundle args = new Bundle();
        args.putString(STORE_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(STORE_PARAM);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_refresh, null);
        Log.i(STORE_PARAM, "in StoreFragment");
        mActivity = getActivity();
        ButterKnifeReset = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new NewsRefreshAdapter(mActivity, mArticleList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount < (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    Log.e("upload hehe", mAdapter.getTopArticleId() + " " + mAdapter.getBottomArticleId());
                    new ArticleTask(mActivity).execute(mAdapter.getBottomArticleId());
                    loading = true;
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("refresh hehe", mAdapter.getTopArticleId() + " " + mAdapter.getBottomArticleId());
                new MoreArticleTask().execute(mAdapter.getTopArticleId());
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new MoreArticleTask().execute(mAdapter.getTopArticleId());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnifeReset = null;
    }

    class MoreArticleTask extends AsyncTask<Long, Void, List<News>> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //Toast.makeText(getContext(), "refreshing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<News> doInBackground(Long... params) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getMoreById(mParam, params[0]);
        }

        @Override
        protected void onPostExecute(List<News> simpleArticleItems) {
            super.onPostExecute(simpleArticleItems);

            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (simpleArticleItems == null || simpleArticleItems.size() == 0) {
                mArticleList.clear();
                mArticleList.addAll(MainActivity.getCacheBase().findAll(mParam.toLowerCase()));
                Log.d("mArtsize",String.valueOf(mArticleList.size()) + " || " + mParam);
                mAdapter.notifyDataSetChanged();//从数据库读取数据
            } else {
                Log.d("update","news");
                mArticleList.clear();
                mArticleList.addAll(simpleArticleItems);
                Toast.makeText(getContext(), "successfully refresh!", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                for(int i = 0;i < mArticleList.size();i++)
                {
                    MainActivity.getCacheBase().insertDetailNews(mArticleList.get(i));//新的写进数据库
                    Log.d("dbwrite","en");
                }
            }
        }
    }




    private List<News> getMoreById(String mParam, Long param) {
        List<News> itemArticleList = new ArrayList<News>();
        Log.d("param",mParam);
        EngAllNewsParser eanp = new EngAllNewsParser(mParam,new String(""));
        eanp.parseData();
        Log.d("debug","parse end");
        NewsFactory parser = null;
        try {
            //Log.d("data",eanp.getJsonArray().getJSONObject(1).toString());
            News a;
            for(int i = 0;i < eanp.getJsonArray().length();i++) {
                parser = new NewsFactory(eanp.getJsonArray().getJSONObject(i));
                a = parser.parserNewsFromJSON();
                itemArticleList.add(a);
            }
        }catch(JSONException e)
        {
            Log.d("exception",e.toString());
        }
        Log.d("debug","return list");
        return itemArticleList;
    }

    class ArticleTask extends AsyncTask<Long, Void, List<News>> {

        private Context mContext;

        public ArticleTask(Context context) {
            mContext = context;
        }

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getContext(), "loading history...", Toast.LENGTH_SHORT).show();
            if (mArticleList != null && mArticleList.size() > 0) {
                mArticleList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                Log.e("up load", "in mArticleList.add(null)");
                mAdapter.notifyItemInserted(mArticleList.size() - 1);
            }
        }
        @Override
        protected List<News> doInBackground(Long... params) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getArticleList(mParam, params[0]);
        }

        @Override
        protected void onPostExecute(final List<News> moreArticles) {
            // 新增新闻数据
            super.onPostExecute(moreArticles);
            if (mArticleList.size() == 0) {
                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();
            } else {
                //删除 footer
                mArticleList.remove(mArticleList.size() - 1);
                mArticleList.addAll(moreArticles);
                mAdapter.notifyDataSetChanged();
                loading = false;
                for(int i = 0;i < moreArticles.size();i++)
                {
                    MainActivity.getCacheBase().insertDetailNews(moreArticles.get(i));
                }
            }
            //Toast.makeText(getContext(), "successfully loaded history", Toast.LENGTH_SHORT).show();
        }

    }

    public List<News> getArticleList(String type, long max_news_id) {
        List<News> itemArticleList = new ArrayList<News>();
        Log.d("param",mParam);
        Log.d("max news id",new Long(max_news_id).toString());
        EngAllNewsParser eanp = new EngAllNewsParser(mParam,new Long(max_news_id).toString());
        eanp.parseData();
        Log.d("debug","parse end");
        NewsFactory parser = null;
        try {
            //Log.d("data",eanp.getJsonArray().getJSONObject(1).toString());
            News a;
            for(int i = 0;i < eanp.getJsonArray().length();i++) {
                parser = new NewsFactory(eanp.getJsonArray().getJSONObject(i));
                a = parser.parserNewsFromJSON();
                itemArticleList.add(a);
            }
        }catch(JSONException e)
        {
            Log.d("exception",e.toString());
        }
        Log.d("debug","return list");
        if(itemArticleList.size() > 0) {
            itemArticleList.remove(0);
        }
        return itemArticleList;
    }
}
