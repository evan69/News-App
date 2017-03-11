package com.ihandy.a2014011433.network;

import android.util.Log;

import com.ihandy.a2014011433.network.DataParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Evan Hou on 2016/8/29.
 * 这个类用来存储某个类别下的所有新闻的json数据
 * 使用一个jsonarray存储
 */
public class EngAllNewsParser extends DataParser {
    final String srcPartly = "http://assignment.crazz.cn/news/query?locale=en&category=";
    String category = "";
    String max_news_id = "";
    JSONArray jsonArray = new JSONArray();
    public EngAllNewsParser(String _category,String _max_news_id)
    {
        super();
        category = _category;
        max_news_id = _max_news_id;
    }

    public JSONArray getJsonArray()
    {
        return jsonArray;
    }

    @Override
    public void parseData()
    {
        MessageReceiver mr = null;
        try {
            URL myUrl;
            if(max_news_id.length() < 1) {
                myUrl = new URL(srcPartly + category);
            }
            else
            {
                myUrl = new URL(srcPartly + category + "&max_news_id=" + max_news_id);
            }
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
                //while(true) {
                //}
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
        //super.data
        try {
            Log.d("news",super.data);
            JSONObject allData = new JSONObject(super.data);
            JSONObject categoryData = allData.getJSONObject("data");
            //super.jsonObject = null;
            try {
                jsonArray = categoryData.getJSONArray("news");
            }catch (org.json.JSONException e) {
                Log.d("json exception",e.toString());
            }
            super.data = jsonArray.toString();
        }catch (Exception e)
        {
            Log.d("exception",e.toString());
        }
    }
}
