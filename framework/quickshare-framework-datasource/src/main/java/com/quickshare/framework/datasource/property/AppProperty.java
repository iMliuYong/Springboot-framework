package com.quickshare.framework.datasource.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AppProperty {

    private DataSourceProperty datasource;

    private DataSourceProperty datasourceSlave;

    private DbSplitProperty dbsplit;

    public DataSourceProperty getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSourceProperty datasource) {
        this.datasource = datasource;
    }

    public DbSplitProperty getDbsplit() {
        return dbsplit;
    }

    public void setDbsplit(DbSplitProperty dbsplit) {
        this.dbsplit = dbsplit;
    }

    public DataSourceProperty getDatasourceSlave() {
        return datasourceSlave;
    }

    public void setDatasourceSlave(DataSourceProperty datasourceSlave) {
        this.datasourceSlave = datasourceSlave;
    }
}