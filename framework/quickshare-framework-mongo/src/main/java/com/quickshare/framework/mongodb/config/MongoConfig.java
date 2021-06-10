package com.quickshare.framework.mongodb.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.quickshare.framework.mongodb.DataSourceCondition;
import com.quickshare.framework.mongodb.dynamic.DynamicMongoDbFactory;
import com.quickshare.framework.mongodb.properties.AppProperty;
import com.quickshare.framework.mongodb.properties.AppsProperty;
import com.quickshare.framework.mongodb.properties.MongoProperties;
import com.quickshare.common.encrytor.DesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

/**
 * @author liu_ke
 */
@Configuration
@Conditional({DataSourceCondition.class})
public class MongoConfig {

    private final static String key = "quckshre";
    private final static String DEFAULT_KEY = "default";

    @Bean
    @ConfigurationProperties(prefix = "mongo")
    public MongoProperties defaultMongoProperties(){
        return new MongoProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "")
    public AppsProperty appsProperty(){
        return new AppsProperty();
    }

    @Bean
    public DynamicMongoDbFactory simpleMongoDbFactory(){
        MongoProperties defaultMongoProperties = defaultMongoProperties();
        AppsProperty appsProperty = appsProperty();
        Map<String,String> dbNameMap = new HashMap<>();
        Map<String, MongoClient> mongoClientMap = generateMongoClients(
                defaultMongoProperties,
                appsProperty.getApp(),
                dbNameMap);
        String defaultKey = DEFAULT_KEY;
        if(!mongoClientMap.containsKey(defaultKey)){
            defaultKey = mongoClientMap.entrySet().iterator().next().getKey();
        }
        return new DynamicMongoDbFactory(mongoClientMap,dbNameMap,defaultKey);
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(simpleMongoDbFactory());
    }

    private MongoClient generateMongoClient(MongoProperties properties){
        if(StringUtils.isEmpty(properties.getHost())){
            return null;
        }
        String pwd = properties.getPassword();
        try{
            pwd = DesUtils.decrypt(pwd,key);
        } catch (Exception e) {
            // do nothing
        }
        MongoCredential credential = MongoCredential.createScramSha1Credential(properties.getUsername(), properties.getDbname(), pwd.toCharArray());
        ServerAddress serverAddress = new ServerAddress(properties.getHost(), properties.getPort());
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->builder.hosts(Collections.singletonList(serverAddress)))
                .credential(credential)
                .build();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        return mongoClient;
    }

    private Map<String, MongoClient> generateMongoClients(
            MongoProperties defaultMongoProperties,
            Map<String, AppProperty> apps,
            Map<String,String> dbNameMap){
        Map<String, MongoClient> mongoClientMap = new HashMap<>();
        MongoClient defaultClient = generateMongoClient(defaultMongoProperties);
        if(defaultClient!=null){
            mongoClientMap.put(DEFAULT_KEY,defaultClient);
            dbNameMap.put(DEFAULT_KEY,defaultMongoProperties.getDbname());
        }
        for (Map.Entry<String,AppProperty> app:apps.entrySet()) {
            if(app.getValue().getMongo() == null){
                continue;
            }
            MongoProperties properties = app.getValue().getMongo();
            MongoClient client = generateMongoClient(properties);
            if(client == null){
                continue;
            }
            mongoClientMap.put(app.getKey(),client);
            dbNameMap.put(app.getKey(),properties.getDbname());
        }
        return mongoClientMap;
    }
}
