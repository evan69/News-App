package com.ihandy.a2014011433.mydb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.ihandy.a2014011433.news.News;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan Hou on 2016/9/5.
 */
public class DetailNewsFavorite {
    public MyFavoriteDb helper;
    public DetailNewsFavorite(Context context){
        helper = new MyFavoriteDb(context);
        helper.getWritableDatabase();
    }
    //插入数据
    public void insertDetailNews(News news){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlCmd = "insert into " + MyFavoriteDb.getDbTable()
                + "("
                + MyFavoriteDb.getDbColumnNewsid() + ","
                + MyFavoriteDb.getDbColumnTitle() + ","
                + MyFavoriteDb.getDbOrigin() + ","
                + MyFavoriteDb.getDbTime() + ","
                + MyFavoriteDb.getDbColumnImaginurl() + ","
                + MyFavoriteDb.getDbColumnUrl() + ")"
                + "values("
                + "'" + news.news_id + "',"
                + "'" + news.title.replace('\'','"') + "',"
                + "'" + news.category + "',"
                + "'" + news.fetched_time + "',"
                + "'" + news.imgURL + "',"
                + "'" + news.source_url + "')";
        Log.e("insert db", sqlCmd);
        db.execSQL(sqlCmd);
        db.close();
    }

    public void del(long news_id){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sqlCmd = "delete from " + MyFavoriteDb.getDbTable() + " where " + MyFavoriteDb.getDbColumnNewsid() + " = '" + news_id + "'";
        db.execSQL(sqlCmd);
        db.close();
    }

    public List<News> findSelected(long news_id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(MyFavoriteDb.getDbTable(),
                MyFavoriteDb.getDbColumnArray(),
                MyFavoriteDb.getDbColumnNewsid() + " = ?",
                new String[] {String.valueOf(news_id)},
                null, null, null);
        List<News> newsArr = new ArrayList<News>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                //NewsItem(long index, String imageUrl, String title, String publishDate, String source, int readTimes, String preview, String contentUrl)
                News order = new News();
                order.news_id = new Long(cursor.getLong(cursor.getColumnIndex(MyFavoriteDb.getDbColumnNewsid()))).toString();
                order.imgURL = new Long(cursor.getColumnIndex(MyFavoriteDb.getDbColumnImaginurl())).toString();
                order.title = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnTitle()));
                order.fetched_time = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbTime()));
                order.category = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbOrigin()));
                order.source_url = cursor.getString(cursor.getColumnIndex(MyFavoriteDb.getDbColumnUrl()));
                newsArr.add(order);
            }
        }
        cursor.close();
        db.close();
        return newsArr;
    }
}
