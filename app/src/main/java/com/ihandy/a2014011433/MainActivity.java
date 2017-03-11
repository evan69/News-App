package com.ihandy.a2014011433;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.common.references.SharedReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.ihandy.a2014011433.R;
import com.ihandy.a2014011433.myadapter.NewsAdapter;
import com.ihandy.a2014011433.mydb.DetailNewsCache;
import com.ihandy.a2014011433.mydb.DetailNewsFavorite;
import com.ihandy.a2014011433.myfragment.NewsFragment;
import com.ihandy.a2014011433.myfragment.NewsRefreshFragment;
import com.ihandy.a2014011433.myfragment.RecyclerViewFragment;
import com.ihandy.a2014011433.network.EngAllNewsParser;
import com.ihandy.a2014011433.network.EngCategoryParser;
import com.ihandy.a2014011433.news.News;
import com.ihandy.a2014011433.news.NewsFactory;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerActivity {

    private MaterialViewPager myViewPager;
    private Toolbar toolbar;
    //static variable and func
    private static DetailNewsFavorite dataBase;
    private static DetailNewsCache cacheBase;
    public static DetailNewsFavorite getDataBase() { return dataBase; }
    public static DetailNewsCache getCacheBase() { return cacheBase; }

    public static News currentNews;
    //---------------------
    //  新闻分类标签
    private ArrayList<String> tabList = new ArrayList<String>();
    //  新闻碎片页面
    private ArrayList<Fragment> newsFrag = new ArrayList<Fragment>();

    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        setTitle("");
        myViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = myViewPager.getToolbar();
        if (toolbar != null){
            //使用Toolbar代替Actionbar
            setSupportActionBar(toolbar);
        }
        dataBase = new DetailNewsFavorite(this);
        cacheBase = new DetailNewsCache(this);
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        boolean isInit = preferences.getBoolean("init",false);
        if(!isInit) {
            Log.d("share","init");
            SharedPreferences.Editor editor = preferences.edit();
            //editor.putString("true");
            editor.putBoolean("init",true);
            editor.putBoolean("sports",true);
            editor.putBoolean("top_stories",true);
            editor.putBoolean("technology",true);
            editor.putBoolean("health",true);
            editor.putBoolean("world",true);
            editor.putBoolean("national",true);
            editor.putBoolean("entertainment",true);
            editor.commit();
            //sports, top_stories, technology, health, world, national, entertainment
        }
        initNavigation();
        new LatestArticleTask().execute();
    }

    // 定义一个long型变量，用于判断两次点击的间隔
    private long exitTime;

    // 实现返回键的点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit(); // 在这里进行点击判断
            return false;
            }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            // 点击间隔大于两秒，做出提示
            Toast.makeText(this, "press again to quit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else {
            // 连续点击量两次，进行应用退出的处理
            System.exit(0);
        }
    }

    void initNavigation(){
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Log.d("tag","ce bian lan");
                    DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                    drawer.closeDrawers();
                    switch (menuItem.getItemId()){
                        case R.id.favorites:
                            Log.d("goin","favorite activity");
                            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                            startActivityForResult(intent, 0);
                            return true;
                        case R.id.category_management:
                            Log.d("lihyapp","category");
                            Intent m_intent = new Intent(MainActivity.this, ChannelActivity.class);
                            startActivityForResult(m_intent, 0);
                            return true;
                        case R.id.about_me:
                            Intent p_intent = new Intent(MainActivity.this, TitleActivity.class);
                            startActivityForResult(p_intent, 0);
                            return true;
                        default:
                            Log.d("bug","bug");
                            return true;
                    }
                }
            });
        }else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            Log.d("on activity result","ret");
            try {
                if (data.getExtras().getBoolean("changed", false)) {
                    new LatestArticleTask().execute();
                }
            }catch (Exception e)
            {

            }
        }
    }

    class LatestArticleTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            Log.d("size","kaokaokao");
            EngCategoryParser ecp = new EngCategoryParser();
            ecp.parseData();
            //tabList = new ArrayList<String>();
            //newsFrag = new ArrayList<Fragment>();
            tabList.clear();
            newsFrag.clear();
            for(int i = 0;i < ecp.getList().size();i++) {
                Log.d("ecp info",ecp.getList().get(i).toString());
                Boolean channel = preferences.getBoolean(ecp.getList().get(i).toString(),false);
                if(channel == false)
                {
                    continue;
                }
                tabList.add(ecp.getList().get(i).toString());
                newsFrag.add(NewsRefreshFragment.newInstance(ecp.getList().get(i).toString()));
            }
            Log.d("size",tabList.toString());
            return tabList;
        }

        @Override
        protected void onPostExecute(List<String> data) {
            super.onPostExecute(data);
            ViewPager viewPager = (ViewPager) myViewPager.getViewPager();
            viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return newsFrag.get(position);
                }

                @Override
                public int getCount() {
                    return tabList.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return tabList.get(position);
                }
            });

            myViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                @Override
                public HeaderDesign getHeaderDesign(int page) {
                    switch (page % 3) {
                        case 0:
                            return HeaderDesign.fromColorResAndUrl(
                                    R.color.green,
                                    "http://img.bzdao.com/136/754079580.jpg");
                        case 1:
                            return HeaderDesign.fromColorResAndUrl(
                                    R.color.blue,
                                    "http://www.bz55.com/uploads/allimg/141114/139-1411141H406.jpg");
                        case 2:
                            return HeaderDesign.fromColorResAndUrl(
                                    R.color.cyan,
                                    "http://pic38.nipic.com/20140217/18010017_162136466126_2.jpg");
                        case 3:
                            return HeaderDesign.fromColorResAndUrl(
                                    R.color.red,
                                    "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    }
                    return null;
                }
            });

            viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
            myViewPager.getPagerTitleStrip().setViewPager(viewPager);

            View logo = findViewById(R.id.logo_white);
            if (logo != null) {
                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myViewPager.notifyHeaderChanged();
                        Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
