package com.ihandy.a2014011433.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Evan Hou on 2016/8/29.
 * 这个类用来存储当前时间下所有的新闻类别
 */
public class EngCategoryParser extends DataParser {
    final String srcPartly = "http://assignment.crazz.cn/news/en/category?timestamp=";
    //JSONObject allCategories = null;
    Vector<String> categoryList = null;
    public EngCategoryParser()
    {
        super();
        categoryList = new Vector<>();
    }

    public Vector getList()
    {
        return categoryList;
    }

    @Override
    public void parseData()
    {
        MessageReceiver mr = null;
        long curTime = System.currentTimeMillis();
        try {
            URL myUrl = new URL(srcPartly + curTime);
            mr = new MessageReceiver(myUrl);
        }catch(MalformedURLException e)
        {
            Log.d("exception",e.toString());
        }

        class MyThread implements Runnable
        {
            MessageReceiver mr;
            MyThread(MessageReceiver _mr)
            {
                mr = _mr;
            }
            public void run()
            {
                mr.receiveData();
            }
        }
        Thread a = new Thread(new MyThread(mr));
        a.start();
        try {
            a.join();
        }
        catch(InterruptedException e)
        {
            Log.d("exception",e.toString());
        }
        super.data = mr.getData();
        Log.d("category",super.data);
        try {
            JSONObject allData = new JSONObject(super.data);
            JSONObject categoryData = allData.getJSONObject("data").getJSONObject("categories");
            Iterator it = categoryData.keys();
            while(it.hasNext())
            {
                String toadd = (String)it.next();
                categoryList.add(toadd.toLowerCase());
            }
            super.data = categoryData.toString();
        }catch (JSONException e)
        {
            Log.d("exception",e.toString());
        }
        finally {
            Log.d("sizecheck","ecp");
            if (categoryList.size() < 1)
            {
                categoryList.add("sports");
                categoryList.add("top_stories");
                categoryList.add("technology");
                categoryList.add("health");
                categoryList.add("world");
                categoryList.add("national");
                categoryList.add("entertainment");
            }
        }
    }
}
