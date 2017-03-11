package com.ihandy.a2014011433.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Evan Hou on 2016/8/29.
 * 这个类是获取数据的基类
 */
public abstract class DataParser {
    String data;
    DataParser()
    {
        data = new String();
    }
    abstract void parseData();
    public String getData()
    {
        return data;
    }
}
