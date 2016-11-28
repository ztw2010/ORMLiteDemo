package com.ormlite.test.ormlitedemo.bean;

/**
 * Created by zhongruan on 2016/11/28.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 测试ormlite的bean
 */

@DatabaseTable(tableName="article")
public class ArticleBean {
    /**
     * TITLE对应的字段名称
     */
    public static final String TITLE = "TITLE";

    /**
     * CONTENT对应的字段名称
     */
    public static final String CONTENT = "CONTENT";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = TITLE)
    private String title;

    @DatabaseField(columnName = CONTENT)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
