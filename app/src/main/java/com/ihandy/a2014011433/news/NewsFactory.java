package com.ihandy.a2014011433.news;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Evan Hou on 2016/8/30.
 */
public class NewsFactory {
    JSONObject jsonObject;
    public NewsFactory(JSONObject _jsonObject)
    {
        jsonObject = _jsonObject;
    }
    public News parserNewsFromJSON()
    {
        News retNews = new News();
        try {
            try {
                retNews.category = jsonObject.getString("category");
            }catch (JSONException e)
            {
                retNews.category = "empty";
            }
            try {
                retNews.country = jsonObject.getString("country");
            }catch (JSONException e)
            {
                retNews.country = "empty";
            }
            try {
                retNews.fetched_time = jsonObject.getString("fetched_time");

            }catch (JSONException e)
            {
                retNews.fetched_time = "empty";
            }
            try {
                retNews.imgURL = (jsonObject.getJSONArray("imgs")).getJSONObject(0).getString("url");

            }catch (JSONException e)
            {
                retNews.imgURL = "empty";
            }
            try {
                retNews.locale_category = jsonObject.getString("locale_category");

            }catch (JSONException e)
            {
                retNews.locale_category = "empty";
            }
            try {
                retNews.news_id = jsonObject.getString("news_id");
            }catch (JSONException e)
            {
                retNews.news_id = "empty";
            }
            try {
                retNews.origin = jsonObject.getString("origin");

            }catch (JSONException e)
            {
                retNews.origin = "empty";
            }
            try {
                retNews.relative_news = jsonObject.getString("relative_news");
            }catch (JSONException e)
            {
                retNews.relative_news = "empty";
            }
            try {
                retNews.title = jsonObject.getString("title");
            }catch (JSONException e)
            {
                retNews.title = "empty";
            }
            try {
                retNews.source_name = jsonObject.getJSONObject("source").getString("name");
                retNews.source_url = jsonObject.getJSONObject("source").getString("url");
                //这里有坑 因为有可能source为null，也就不能再geiString
            }catch (JSONException e)
            {
                //e.printStackTrace();
                retNews.source_name = "empty";
                retNews.source_url = "empty";
            }
            retNews.updated_time = jsonObject.getString("updated_time");
        }catch(JSONException e)
        {
            Log.d("exception",e.toString());
        }
        return retNews;
    }
}
