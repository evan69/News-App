package com.ihandy.a2014011433.news;

/**
 * Created by Evan Hou on 2016/8/30.
 */
public class News {
    public String category = "";
    public String country = "";
    public String fetched_time = "";
    public String imgURL = "";
    public String locale_category = "";
    public String news_id = "";
    public String origin = "";
    public String relative_news = "";
    public String source_name,source_url = "";
    public String title = "";
    public String updated_time = "";

    public News(){
    }

    @Override
    public String toString()
    {
        String retStr = new String();
        retStr += "category:" + category.toLowerCase() + "\n";
        retStr += "country:" + country + "\n";
        retStr += "fetched_time:" + fetched_time + "\n";
        retStr += "imgURL:" + imgURL + "\n";
        retStr += "locale_category:" + locale_category + "\n";
        retStr += "news_id:" + news_id + "\n";
        retStr += "origin:" + origin + "\n";
        retStr += "relative_news:" + relative_news + "\n";
        retStr += "source_name:" + source_name + "\n";
        retStr += "source_url:" + source_url + "\n";
        retStr += "title:" + title + "\n";
        retStr += "updated_time:" + updated_time + "\n";
        return retStr;
    }
}