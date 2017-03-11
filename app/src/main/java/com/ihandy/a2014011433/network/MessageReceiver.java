package com.ihandy.a2014011433.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Evan Hou on 2016/8/29.
 * 通过构造函数传入url
 * 首先receiveDate然后getDate
 */
public class MessageReceiver {
    URL source;
    String data = "";
    MessageReceiver(URL _source)
    {
        source = _source;
    }
    void receiveData()
    {
        try {
            URLConnection conn = source.openConnection();
            InputStream is = conn.getInputStream();
            InputStreamReader ir = new InputStreamReader(is);
            StringBuffer str = new StringBuffer();
            int c;
            while((c = ir.read()) != -1)
            {
                str.append((char)c);
            }
            data = new String(str);
        }catch(IOException e)
        {
            Log.d("debug",e.toString());
        }
    }
    String getData()
    {
        return data;
    }
}
