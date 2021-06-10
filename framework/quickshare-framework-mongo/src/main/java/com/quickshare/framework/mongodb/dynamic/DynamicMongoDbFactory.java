package com.quickshare.framework.mongodb.dynamic;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Map;

/**
 * @author liu_ke
 */
public class DynamicMongoDbFactory extends MongoDatabaseFactorySupport<MongoClient> implements DisposableBean {

    private final Map<String,MongoClient> clientMap;
    private final Map<String,String> dbNameMap;

    public DynamicMongoDbFactory(Map<String,MongoClient> clientMap,
                                 Map<String,String> dbNameMap,
                                 String defaultKey) {
        super(clientMap.get(defaultKey), dbNameMap.get(defaultKey), false, new MongoExceptionTranslator());
        this.clientMap = clientMap;
        this.dbNameMap = dbNameMap;
        DynamicDataSourceContextHolder.addDataSourceKeys(defaultKey,clientMap.keySet());

        SimpleMongoClientDatabaseFactory a;
    }

    @Override
    public MongoDatabase getMongoDatabase() throws DataAccessException {
        String key = DynamicDataSourceContextHolder.getDataSourceKey();
        return clientMap.get(key).getDatabase(dbNameMap.get(key));
    }

    @Override
    public MongoDatabase getMongoDatabase(String s) throws DataAccessException {
        return doGetMongoDatabase(s);
    }

    @Override
    protected MongoDatabase doGetMongoDatabase(String s) {
        return getMongoClient().getDatabase(s);
    }

    @Override
    protected MongoClient getMongoClient() {
        String key = DynamicDataSourceContextHolder.getDataSourceKey();
        return clientMap.get(key);
    }

    @Override
    public ClientSession getSession(ClientSessionOptions clientSessionOptions) {
        return (this.getMongoClient()).startSession(clientSessionOptions);
    }

    @Override
    protected void closeClient() {
        for (Map.Entry<String,MongoClient> clientInfo: clientMap.entrySet()) {
            clientInfo.getValue().close();
        }
    }
}
