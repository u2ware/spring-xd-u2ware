package io.github.u2ware.xd.ibs.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@RestController
public class MondodbRestController implements EnvironmentAware{

    protected Log logger = LogFactory.getLog(getClass());

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
    
    @Autowired
	private Mongo mongo;
	private Map<String, MongoTemplate> mongoTemplates = Maps.newHashMap();
    
    @RequestMapping(value="/helloworld", produces = "text/html; charset=utf-8")
    public String world(HttpServletRequest request) {
    	logger.info("helloworld");
    	logger.info(""+environment);
    	logger.info(""+mongo);
    	try{
    		for(String databaseName : mongo.getDatabaseNames()){
    	    	logger.info(""+databaseName);
    		}
    	}catch(Exception e){
    		logger.info("", e);
    	}
        return "hello world";
    }

    @RequestMapping(value="/")
	public Page<DBObject> database() throws Exception{
		
    	List<DBObject> content = Lists.newArrayList();
		for(String databaseName : mongo.getDatabaseNames()){
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			content.add(obj);
		}
		return new PageImpl<DBObject>(content);
	}
    
    @RequestMapping(value="/{databaseName}")
	public Page<DBObject> collections(
			@PathVariable("databaseName") String databaseName) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

    	List<DBObject> content = Lists.newArrayList();
		for(String collectionName : mongoTemplate.getCollectionNames()){
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			obj.put("collectionName", collectionName);
			content.add(obj);
		}
		return new PageImpl<DBObject>(content);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}")
	public Page<DBObject> entities(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			Pageable pageable) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		

		//Query query = new BasicQuery(requestBody).with(pageable);
		Query query = new Query().with(pageable);
		List<DBObject> content = mongoTemplate.find(query, DBObject.class, collectionName);
		Long count = mongoTemplate.getCollection(collectionName).count();
		return new PageImpl<DBObject>(content, pageable, count);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}/{id}")
	public DBObject entity(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

		DBObject result = mongoTemplate.findById(id, DBObject.class, collectionName);
		return result;
	}
    
	protected MongoTemplate getMongoTemplate(String databaseName) {
		MongoTemplate mongoTemplate = mongoTemplates.get(databaseName);
		if(mongoTemplate == null){
			mongoTemplate = new MongoTemplate(this.mongo, databaseName);
			mongoTemplates.put(databaseName.toString(), mongoTemplate);
		}
		return mongoTemplate;
	}
}
