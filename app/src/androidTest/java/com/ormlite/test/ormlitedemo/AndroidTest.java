package com.ormlite.test.ormlitedemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.ormlite.test.ormlitedemo.bean.ArticleBean;
import com.ormlite.test.ormlitedemo.db.LazyDB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongruan on 2016/11/28.
 */

@RunWith(AndroidJUnit4.class)
public class AndroidTest {
    private static final String TAG = "AndroidTest";

    private Context context = null;

    /**
     * 带 @Before 的方法是每个单元测试都必须优先走的第一个
     */
    @Before
    public void init() {
        Log.d(TAG, "init() called");

        // 在单元测试中获取 上下文对方的方法，当然也可以直接获取 Application 对象
        context = InstrumentationRegistry.getTargetContext();
        initDB();
    }

    /**
     * 带 @After 的方法是每个单元测试 结束后都必须走的
     */
    @After
    public void end() {
        Log.d(TAG, "end() called");
        LazyDB.getSingleton().close();
    }

    @Test
    public void testInsert() {
        LazyDB lazyDB = LazyDB.getSingleton();
        ArticleBean articleBean = new ArticleBean();
        articleBean.setContent("文章Content");
        articleBean.setTitle("文章Title");
        int insert = lazyDB.insert(ArticleBean.class, articleBean);
        Assert.assertNotEquals(0, insert);
    }

    @Test
    public void testBatchInsert() {
        LazyDB lazyDB = LazyDB.getSingleton();
        List<ArticleBean> articleBeens = new ArrayList<ArticleBean>();
        for(int i = 0; i < 10; i ++){
            ArticleBean articleBean = new ArticleBean();
            articleBean.setContent("文章Content 索引="+i);
            articleBean.setTitle("文章Title 索引="+i);
            articleBeens.add(articleBean);
        }
        int insert = lazyDB.batchInsert(articleBeens);
        Assert.assertEquals(articleBeens.size(), insert);
    }

    @Test
    public void testDelete() {
        LazyDB lazyDB = LazyDB.getSingleton();
        ArticleBean articleBean = new ArticleBean();
        articleBean.setContent("文章Content2");
        articleBean.setTitle("文章Title2");
        lazyDB.insert(ArticleBean.class, articleBean);
        try {
            List<ArticleBean> articleBeen = lazyDB.getQueryBuilder(ArticleBean.class).where().eq(ArticleBean.CONTENT, "文章Content2").and().eq(ArticleBean.TITLE, "文章Title2").query();
            Assert.assertNotNull(articleBeen);
            Assert.assertNotEquals(0, articleBeen.size());
            ArticleBean deleteArticleBean = articleBeen.iterator().next();
            int delete = lazyDB.delete(ArticleBean.class, deleteArticleBean);
            Assert.assertNotEquals(0, delete);
        } catch (SQLException e) {

        }
    }

    @Test
    public void testUpdate() {
        LazyDB lazyDB = LazyDB.getSingleton();
        ArticleBean articleBean = new ArticleBean();
        articleBean.setContent("文章Content2");
        articleBean.setTitle("文章Title2");
        lazyDB.insert(ArticleBean.class, articleBean);
        try {
            List<ArticleBean> articleBeen = lazyDB.getQueryBuilder(ArticleBean.class).where().eq(ArticleBean.CONTENT, "文章Content2").and().eq(ArticleBean.TITLE, "文章Title2").query();
            Assert.assertNotNull(articleBeen);
            Assert.assertNotEquals(0, articleBeen.size());
            ArticleBean deleteArticleBean = articleBeen.iterator().next();
            deleteArticleBean.setTitle("new 文章Content2");
            int update = lazyDB.updateId(ArticleBean.class, deleteArticleBean, deleteArticleBean.getId());
            List<ArticleBean> articleBeen1 = lazyDB.queryForAll(ArticleBean.class);
            Assert.assertNotEquals(0, update);
        } catch (SQLException e) {

        }
    }

    @Test
    public void testQuery() {
        LazyDB lazyDB = LazyDB.getSingleton();
        ArticleBean articleBean = new ArticleBean();
        articleBean.setContent("insert 文章Content2");
        articleBean.setTitle("insert 文章Title2");
        lazyDB.insert(ArticleBean.class, articleBean);
        try {
            ArticleBean articleBeen = lazyDB.getQueryBuilder(ArticleBean.class).where().eq(ArticleBean.CONTENT, "insert 文章Content2").and().eq(ArticleBean.TITLE, "insert 文章Title2").queryForFirst();
            Assert.assertNotNull(articleBeen);
        } catch (SQLException e) {

        }
    }

    /**
     * 初始化DB
     *
     * @return
     */
    private void initDB() {
        String dbName = "test.db";
        int dbVersion = 1;
        boolean saveOnSDCard = true;
        LazyDB lazyDB = LazyDB.getSingleton();
        lazyDB.initLazyDB(context, 20);
    }
}
