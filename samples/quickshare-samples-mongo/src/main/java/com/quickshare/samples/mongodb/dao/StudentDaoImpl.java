package com.quickshare.samples.mongodb.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.quickshare.samples.mongodb.pojo.Classes;
import com.quickshare.samples.mongodb.pojo.Student;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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
public class StudentDaoImpl implements StudentDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Student object, String collectionName) {
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public void add(String classesId, Object student) {
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(classesId)), new Update().addToSet("students", student), Classes.class,"classes");
    }

    @Override
    public Student findOne(Map<String,Object> params, String collectionName) {
        String id = (String)params.get("students.id");
        Criteria criteria = Criteria.where("students").elemMatch(Criteria.where("id").is(id));
        Query query = new Query(criteria);
        query.fields().include("students");
        Classes result = mongoTemplate.findOne(query, Classes.class,collectionName);
        if(result == null){
            return null;
        }
        else {
            return result.getStudents().get(0);
        }
    }

    @Override
    public List<Student> findAll(Map<String,Object> params, String collectionName) {
        Query query ;
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
        query.fields().include("students");
        Classes result = mongoTemplate.findOne(query, Classes.class,collectionName);
        if(result == null){
            return null;
        }
        else {
            return result.getStudents();
        }
    }

    @Override
    public void update(Map<String,Object> params,String collectionName) {
        mongoTemplate.upsert(new Query(Criteria.where("id").is(params.get("id"))), new Update().set("name", params.get("name")), Student.class,collectionName);
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
