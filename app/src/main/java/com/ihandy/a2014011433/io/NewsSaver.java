package com.ihandy.a2014011433.io;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ihandy.a2014011433.R;
import com.ihandy.a2014011433.news.News;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Evan Hou on 2016/9/1.
 */

public class NewsSaver{// extends Activity{
    News news;
    Context context;
    public NewsSaver(News _news,Context _context)
    {
        //super();
        news = _news;
        context = _context;
    }

    public void saveNews()
    {
        try {
            Log.d("title",news.title);
            FileOutputStream os = context.openFileOutput("test.txt", Context.MODE_PRIVATE);
            os.write(news.toString().getBytes());
            os.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
