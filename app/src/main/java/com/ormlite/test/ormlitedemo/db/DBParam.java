package com.ormlite.test.ormlitedemo.db;

/**
 * 数据库信息配置
 * 
 * @author ztw
 */
public class DBParam
{
    /**
     * 数据库名字
     */
    private String dbName = "test.db";
    
    /**
     * 数据库版本,默认为1
     */
    private int dbVersion = 1;

    /**
     * 是否保存到SD卡
     */
    private boolean saveOnSDCard = false;
    
    /**
     * 数据库文件在sd卡中的目录
     */
    private String targetDirectory = null;
    
    public String getDbName()
    {
        return dbName;
    }
    
    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }
    
    public int getDbVersion()
    {
        return dbVersion;
    }
    
    public void setDbVersion(int dbVersion)
    {
        this.dbVersion = dbVersion;
    }

    public boolean isSaveOnSDCard() {
        return saveOnSDCard;
    }

    public void setSaveOnSDCard(boolean saveOnSDCard) {
        this.saveOnSDCard = saveOnSDCard;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public DBParam(String dbName, int dbVersion, boolean saveOnSDCard, String targetDirectory) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.saveOnSDCard = saveOnSDCard;
        this.targetDirectory = targetDirectory;
    }

    public DBParam() {

    }
}
