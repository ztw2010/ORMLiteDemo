package com.ormlite.test.ormlitedemo.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * 数据更新是执行该操作
 * 
 * @author  ztw
 */
public interface DBUpdateListener
{
    
    /**
     * 数据库更新回调方法
     * 
     * @param db 数据库
     * @param connectionSource 数据连接资源
     * @param oldVersion 老版本
     * @param newVersion 新版
     * @see [类、类#方法、类#成员]
     */
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion);
}
