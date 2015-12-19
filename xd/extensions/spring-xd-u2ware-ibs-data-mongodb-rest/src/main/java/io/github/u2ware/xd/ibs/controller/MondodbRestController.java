package io.github.u2ware.xd.ibs.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
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
public class MondodbRestController {

	//MappingMongoConverter a;
	
    protected Log logger = LogFactory.getLog(getClass());

	//private static String[] allowClassNames = new String[]{CurrentData.class.getName(), PostData.class.getName()};

	private @Autowired Mongo mongo;
    private Map<String, MongoTemplate> mongoTemplates = Maps.newHashMap();
    
    @RequestMapping(value="/")
	public Page<DBObject> database() throws Exception{
		
    	List<DBObject> content = Lists.newArrayList();
		for(String databaseName : mongo.getDatabaseNames()){
			
			int collectionCount = mongo.getDB(databaseName).getCollectionNames().size();
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			obj.put("collectionCount", collectionCount);
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

			BasicDBObject q = new BasicDBObject();
			q.append("_class", CurrentData.class.getName());
			
			Query query = new BasicQuery(q);
			Long documentCount = mongoTemplate.count(query, CurrentData.class, collectionName);
			
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			obj.put("collectionName", collectionName);
			obj.put("documentCount", documentCount);
			content.add(obj);
		}
		return new PageImpl<DBObject>(content);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}")
	public Page<CurrentData> documents(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			Pageable pageable) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		
		BasicDBObject q = new BasicDBObject();
		q.append("_class", CurrentData.class.getName());
		Query query = new BasicQuery(q).with(pageable);

		Long count = mongoTemplate.count(query, CurrentData.class, collectionName);
		List<CurrentData> content = mongoTemplate.find(query, CurrentData.class, collectionName);
		return new PageImpl<CurrentData>(content, pageable, count);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}/{id}")
    //@RequestMapping(value="/{databaseName}/{id}/{timestamp}")
	public CurrentData document(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

		CurrentData result = mongoTemplate.findById(id, CurrentData.class, collectionName);
		return result;
		
		/*
		logger.info("document: "+databaseName);
		logger.info("document: "+collectionName);
		logger.info("document: "+id);
		
		BasicDBObject q1 = new BasicDBObject();
		q1.append("_class", allowClassNames[0]);
		q1.append("_id", collectionName);

		Query query1 = new BasicQuery(q1);
		boolean exists = mongoTemplate.exists(query, entityClass, databaseName);

		logger.info("document: "+exists);
		
		if(exists){
			//history...
			BasicDBObject q = new BasicDBObject();
			q.append("usage", "history");
			q.append("_id", Long.parseLong(id));
			
	    	Query query = new BasicQuery(q);
			
	    	DBObject result = mongoTemplate.findOne(query, DBObject.class, collectionName);
			logger.info("document: "+result);
			return result;
			
		}else{
			//current...
			BasicDBObject q = new BasicDBObject();
			q.append("usage", "current");
			q.append("_id", id);
	    	Query query = new BasicQuery(q);
			
	    	DBObject result = mongoTemplate.findOne(query, DBObject.class, collectionName);
			logger.info("document: "+result);
			return result;
		}
		*/
	}
    
	protected MongoTemplate getMongoTemplate(String databaseName) {
		MongoTemplate mongoTemplate = mongoTemplates.get(databaseName);
		if(mongoTemplate == null){
			mongoTemplate = new MongoTemplate(this.mongo, databaseName);
			mongoTemplates.put(databaseName.toString(), mongoTemplate);
		}
		return mongoTemplate;
	}

//    private static HttpServletRequest request(){
//    	return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//    }
}
