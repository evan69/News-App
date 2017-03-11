package com.ihandy.a2014011433.myfragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.ihandy.a2014011433.R;
//import com.ihandy.a2014011433.myadapter.ItemArticleAdapter;
import com.ihandy.a2014011433.myadapter.NewsAdapter;
import com.ihandy.a2014011433.network.EngAllNewsParser;
import com.ihandy.a2014011433.news.News;
import com.ihandy.a2014011433.news.NewsFactory;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evan Hou on 2016/9/1.
 */
public class NewsFragment extends Fragment {
    Unbinder ButterKnifeReset;
    private static final String STORE_PARAM = "param";
    @BindView(R.id.news_article)
    RecyclerView newsArticle;
    private String mParam;
    private List<News> itemArticleList = new ArrayList<News>();
    private Activity mAct;
    public static Fragment newInstance(String param) {
        NewsFragment fragment = new NewsFragment();
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
        newsArticle.addItemDecoration(new MaterialViewPagerHeaderDecorator());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        //Log.i(STORE_PARAM, "in StoreFragment");
        mAct = getActivity();
        ButterKnifeReset = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsArticle.setLayoutManager(new LinearLayoutManager(mAct));//这里用线性显示 类似于listview
        new LatestArticleTask().execute();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnifeReset = null;
    }

    class LatestArticleTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<News> doInBackground(String... params) {
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

        @Override
        protected void onPostExecute(List<News> data) {
            super.onPostExecute(data);
            NewsAdapter adapter = new NewsAdapter(mAct, data);
            newsArticle.setAdapter(adapter);
        }
    }

}
