package io.github.u2ware.xd.ibs.controller;

import io.github.u2ware.xd.ibs.CurrentData;
import io.github.u2ware.xd.ibs.PostData;

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

	private static String[] allowClassNames = new String[]{CurrentData.class.getName(), PostData.class.getName()};

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
			q.append("_class", new BasicDBObject("$in", allowClassNames));
			
			Query query = new BasicQuery(q);
			//Query query = new BasicQuery("{\"_class\":\"io.github.u2ware.xd.ibs.Data\"}");
			Long documentCount = mongoTemplate.count(query, collectionName);
			
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			obj.put("collectionName", collectionName);
			obj.put("documentCount", documentCount);
			content.add(obj);
		}
		return new PageImpl<DBObject>(content);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}")
	public Page<DBObject> documents(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			Pageable pageable) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		
		BasicDBObject q = new BasicDBObject();
		q.append("_class", new BasicDBObject("$in", allowClassNames));
		
		Query query = new BasicQuery(q).with(pageable);
		//Query query = new BasicQuery("{\"_class\":\"io.github.u2ware.xd.ibs.Data\"}").with(pageable);

		Long count = mongoTemplate.count(query, collectionName);
		List<DBObject> content = mongoTemplate.find(query, DBObject.class, collectionName);
		return new PageImpl<DBObject>(content, pageable, count);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}/{id}")
	public Object document(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);


		logger.info("document: "+databaseName);
		logger.info("document: "+collectionName);
		logger.info("document: "+id);
		
		BasicDBObject q1 = new BasicDBObject();
		q1.append("_class", allowClassNames[0]);
		q1.append("_id", collectionName);
    	Query query1 = new BasicQuery(q1);
		
		boolean exists = mongoTemplate.exists(query1, databaseName);

		logger.info("document: "+exists);
		
		if(exists){
			//history...
			BasicDBObject q = new BasicDBObject();
			q.append("_class", allowClassNames[1]);
			q.append("_id", Long.parseLong(id));
			
	    	Query query = new BasicQuery(q);
			
			PostData result = mongoTemplate.findOne(query, PostData.class, collectionName);
			logger.info("document: "+result);
			return result;
			
		}else{
			//current...
			BasicDBObject q = new BasicDBObject();
			q.append("_class", allowClassNames[0]);
			q.append("_id", id);
	    	Query query = new BasicQuery(q);
			
			CurrentData result = mongoTemplate.findOne(query, CurrentData.class, collectionName);
			logger.info("document: "+result);
			return result;
		}
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
