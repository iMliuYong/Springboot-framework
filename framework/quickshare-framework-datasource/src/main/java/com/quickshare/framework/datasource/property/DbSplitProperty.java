package com.quickshare.framework.datasource.property;

/**
 * @author liu_ke
 */
public class DbSplitProperty {

    private DataSourceProperty datasource;

    private DbSplitAccord accord;

    public DataSourceProperty getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSourceProperty datasource) {
        this.datasource = datasource;
    }

    public DbSplitAccord getAccord() {
        return accord;
    }

    public void setAccord(DbSplitAccord accord) {
        this.accord = accord;
    }
}
