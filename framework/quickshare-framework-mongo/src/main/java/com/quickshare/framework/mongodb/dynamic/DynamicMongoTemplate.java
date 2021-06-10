package com.quickshare.framework.mongodb.dynamic;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author liu_ke
 */
public class DynamicMongoTemplate extends MongoTemplate {

    private static ThreadLocal<MongoDatabaseFactory> mongoDbFactoryThreadLocal;

    public DynamicMongoTemplate(MongoDatabaseFactory mongoDbFactory){
        super(mongoDbFactory);
        if(mongoDbFactoryThreadLocal==null) {
            mongoDbFactoryThreadLocal = new ThreadLocal<>();
        }
    }

    public void setMongoDbFactory(MongoDatabaseFactory factory){
        mongoDbFactoryThreadLocal.set(factory);
    }

    public void removeMongoDbFactory(){
        mongoDbFactoryThreadLocal.remove();
    }

    @Override
    public MongoDatabase getDb() {
        return mongoDbFactoryThreadLocal.get().getMongoDatabase();
    }
}
