
package com.ormlite.test.ormlitedemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 管理数据库的创建和版本更新
 * 
 * @author ztw
 */
public class LazyDBHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = "LazyDBHelper";

	private static final int MAX_MENORY_SIZE = 10;
	/** 内存缓存 */
	private Map<String, Dao<?, ?>> lruMemoryCache;
	/**
	 * 数据库更新监听器
	 */
	private DBUpdateListener dbUpdateListener;

	/**
	 * 数据库创建监听
	 */
	private DBCreateListener dbCreateListener;

	/**
	 * @param <T>
	 * @param <ID>
	 * @param context 上下文
	 * @param databaseName 数据库名
	 * @param factory 可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param databaseVersion 数据库版本
	 * @param configFile 数据库存储路径
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public <T, ID> LazyDBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion, File configFile,int daoMaxSize) {
		super(context, databaseName, factory, databaseVersion, configFile);
		lruMemoryCache = new HashMap<String, Dao<?, ?>>();
	}

	/**
	 * @param <T>
	 * @param <ID>
	 * @param context 上下文
	 * @param databaseName 数据库名
	 * @param factory 可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param databaseVersion 数据库版本
	 * @param daoMaxSize dao的最大缓存数量(小于等于0则使用默认,默认为10)
	 */
	public <T, ID> LazyDBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion,int daoMaxSize) {
		super(context, databaseName, factory, databaseVersion);
		lruMemoryCache = new HashMap<String, Dao<?, ?>>();
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		if (dbCreateListener != null) {
			dbCreateListener.onCreate(db, connectionSource);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		if (dbUpdateListener != null) {
			dbUpdateListener.onUpgrade(db, connectionSource, oldVersion,
					newVersion);
		}
	}

	@Override
	public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
			throws SQLException {
		D d = null;
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.containsKey(clazzName)) {
			return (D) lruMemoryCache.get(clazzName);
		} else {
			TableUtils.createTableIfNotExists(getConnectionSource(), clazz);
			d = super.getDao(clazz);
			lruMemoryCache.put(clazzName, d);
		}
		return d;
	}

	/**
	 * 设置数据库升级监听器
	 * 
	 * @param dbUpdateListener
	 *            监听器
	 * @see [类、类#方法、类#成员]
	 */
	public void setDbUpdateListener(DBUpdateListener dbUpdateListener) {
		this.dbUpdateListener = dbUpdateListener;
	}

	/***
	 * 设置数据库创建监听器
	 * 
	 * @param dbCreateListener
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setDbCreateListener(DBCreateListener dbCreateListener) {
		this.dbCreateListener = dbCreateListener;
	}
	
	/**
	 * 删除表
	 * @param clazz 
	 * @param ignoreErrors 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void dropTable(Class<T> clazz,boolean ignoreErrors){
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.containsKey(clazzName)) {
			lruMemoryCache.remove(clazzName);
			try {
				TableUtils.dropTable(getConnectionSource(), clazz, ignoreErrors);
			} catch (SQLException e) {
				Log.e(TAG, "删除表错误");
			}
		}
	}
	
	/**
	 * 删除表
	 * @param tableConfig 
	 * @param ignoreErrors 是否忽视错误
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void dropTable(DatabaseTableConfig<T> tableConfig, boolean ignoreErrors){
		String clazzName = tableConfig.getDataClass().getSimpleName();
		if (lruMemoryCache.containsKey(clazzName)) {
			lruMemoryCache.remove(clazzName);
			try {
				TableUtils.dropTable(getConnectionSource(), tableConfig, ignoreErrors);
			} catch (SQLException e) {
				Log.e(TAG, "删除表错误");
			}
		}
	}
	
	/**
	 * 清理表
	 * @param clazz 类
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(Class<T> clazz){
		String clazzName = clazz.getSimpleName();
		if (lruMemoryCache.containsKey(clazzName)) {
			try {
				TableUtils.clearTable(getConnectionSource(), clazz);
			} catch (SQLException e) {
				Log.e(TAG, "清理表错误");
			}
		}
	}
	
	/**
	 * 清理表
	 * @param tableConfig
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> void clearTable(DatabaseTableConfig<T> tableConfig){
		String clazzName = tableConfig.getDataClass().getSimpleName();
		if (lruMemoryCache.containsKey(clazzName)) {
			try {
				TableUtils.clearTable(getConnectionSource(), tableConfig);
			} catch (SQLException e) {
				Log.e(TAG, "清理表错误");
			}
		}
	}

	/***
	 * 获取查询语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             QueryBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> QueryBuilder<T, ?> getQueryBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return getDao(clazz).queryBuilder();
	}

	/***
	 * 获取删除语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             DeleteBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> DeleteBuilder<T, ?> getDeleteBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return getDao(clazz).deleteBuilder();
	}

	/***
	 * 获取更新语句构造器
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 *             DeleteBuilder<T,?>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <T> UpdateBuilder<T, ?> getUpdateBuilder(Class<T> clazz)
			throws SQLException, NullPointerException {
		return getDao(clazz).updateBuilder();
	}

	/**
	 * 批量插入
	 * @param t
	 * @param <T>
     */
	public <T> int batchInsert(final List<T> t) throws Exception {
		T cla = t.iterator().next();
		final Dao dao = getDao(cla.getClass());
		final Object o = dao.callBatchTasks(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return dao.create(t);
			}
		});
		return (Integer)o;
	}
	
	@Override
	public void close() {
		super.close();
		if (lruMemoryCache != null) {
			lruMemoryCache.clear();
			lruMemoryCache = null;
		}
		if (connectionSource != null) {
			connectionSource.close();
			connectionSource = null;
		}
		DaoManager.clearCache();
		DaoManager.clearDaoCache();
		dbCreateListener = null;
		dbUpdateListener = null;
	}

}
