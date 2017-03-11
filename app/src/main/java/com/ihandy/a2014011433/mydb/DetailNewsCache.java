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
public class DetailNewsCache {
    public MyCacheDb helper;
    public DetailNewsCache(Context context){
        helper = new MyCacheDb(context);   //与数据库建立连接
        helper.getWritableDatabase();
    }
    //插入数据
    public void insertDetailNews(News news){
        if(findSelected(Long.parseLong(news.news_id)).size() > 0) return;
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlCmd = "insert into " + MyCacheDb.getDbTable()
                + "("
                + MyCacheDb.getDbColumnNewsid() + ","
                + MyCacheDb.getDbColumnTitle() + ","
                + MyCacheDb.getDbOrigin() + ","
                + MyCacheDb.getDbTime() + ","
                + MyCacheDb.getDbColumnImaginurl() + ","
                + MyCacheDb.getDbColumnUrl() + ")"
                + "values("
                + "'" + news.news_id + "',"
                + "'" + news.title.replace('\'','"') + "',"
                + "'" + news.category.toLowerCase().replace('_',' ') + "',"
                + "'" + news.fetched_time + "',"
                + "'" + news.imgURL + "',"
                + "'" + news.source_url + "')";
        Log.e("insert db", sqlCmd);
        db.execSQL(sqlCmd);
        db.close();
    }

    //删除数据
    public void del(long news_id){   //根据传入参数newsid删除数据
        SQLiteDatabase db = helper.getReadableDatabase();
        String sqlCmd = "delete from " + MyCacheDb.getDbTable() + " where " + MyCacheDb.getDbColumnNewsid() + " = '" + news_id + "'";
        Log.e("del db", sqlCmd);
        db.execSQL(sqlCmd);
        db.close();
    }

    //查询数据
    public List<News> findSelected(long news_id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(MyCacheDb.getDbTable(),
                MyCacheDb.getDbColumnArray(),
                MyCacheDb.getDbColumnNewsid() + " = ?",
                new String[] {String.valueOf(news_id)},
                null, null, null);
        Log.e("find db", String.valueOf(cursor.getCount()));
        List<News> newsArr = new ArrayList<News>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                News order = new News();
                order.news_id = new Long(cursor.getLong(cursor.getColumnIndex(MyCacheDb.getDbColumnNewsid()))).toString();
                order.imgURL = new Long(cursor.getColumnIndex(MyCacheDb.getDbColumnImaginurl())).toString();
                order.title = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbColumnTitle()));
                order.fetched_time = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbTime()));
                order.category = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbOrigin()));
                order.source_url = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbColumnUrl()));
                newsArr.add(order);
            }
        }
        cursor.close();
        db.close();
        return newsArr;
    }

    //查询某类所有数据
    public List<News> findAll(String category){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(MyCacheDb.getDbTable(),
                MyCacheDb.getDbColumnArray(),
                MyCacheDb.getDbOrigin() + " = ?",
                new String[] {category.toLowerCase().replace('_',' ')},
                null, null, null);
        Log.e("find db", String.valueOf(cursor.getCount()));
        List<News> newsArr = new ArrayList<News>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                News order = new News();
                order.news_id = new Long(cursor.getLong(cursor.getColumnIndex(MyCacheDb.getDbColumnNewsid()))).toString();
                order.imgURL = new Long(cursor.getColumnIndex(MyCacheDb.getDbColumnImaginurl())).toString();
                order.title = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbColumnTitle()));
                order.fetched_time = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbTime()));
                order.category = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbOrigin()));
                order.source_url = cursor.getString(cursor.getColumnIndex(MyCacheDb.getDbColumnUrl()));
                newsArr.add(order);
            }
        }
        cursor.close();
        db.close();
        return newsArr;
    }
}
