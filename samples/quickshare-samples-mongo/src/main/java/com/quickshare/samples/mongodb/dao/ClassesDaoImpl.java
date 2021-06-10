package com.quickshare.samples.mongodb.dao;

import com.mongodb.BasicDBObject;
import com.quickshare.samples.mongodb.pojo.Classes;
import com.quickshare.samples.mongodb.pojo.Student;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author liu_ke
 */
@Repository
public class ClassesDaoImpl implements ClassesDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Classes object, String collectionName) {
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public Classes findOne(Map<String,Object> params, String collectionName) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(params.get("id"))), Classes.class,collectionName);
    }

    @Override
    public List<Classes> findAll(Map<String,Object> params, String collectionName) {
        Query query;
        if(!params.isEmpty()){
            Iterator<Map.Entry<String,Object>> iterator = params.entrySet().iterator();
            Map.Entry<String,Object> param = iterator.next();
            Criteria criteria= Criteria.where(param.getKey()).is(param.getValue());
            while (iterator.hasNext()){
                param = iterator.next();
                criteria= criteria.andOperator(Criteria.where(param.getKey()).is(param.getValue()));
            }
            query = new Query(criteria);
        }
        else{
            query = new Query();
        }
        List<Classes> result = mongoTemplate.find(query, Classes.class,collectionName);
        return result;
    }

    @Override
    public void update(Map<String,Object> params,String collectionName) {
        Document basicDBObject=new Document();
        basicDBObject.put("$set", new Document(params));
        Update update=new BasicUpdate(basicDBObject);
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(params.get("id"))), update, Classes.class,collectionName);
    }

    @Override
    public void createCollection(String name) {
        mongoTemplate.createCollection(name);
    }


    @Override
    public void remove(Map<String, Object> params,String collectionName) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(params.get("id"))),Student.class,collectionName);
    }

}
