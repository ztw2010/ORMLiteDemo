package com.ormlite.test.ormlitedemo.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;


/**
 * 数据库创建监听
 *
 * @author  ztw
 */
public interface DBCreateListener
{
    /**
     * 数据库更新回调方法
     * 
     * @param db 数据库
     * @param connectionSource 数据连接资源
     * @see [类、类#方法、类#成员]
     */
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource);
}
