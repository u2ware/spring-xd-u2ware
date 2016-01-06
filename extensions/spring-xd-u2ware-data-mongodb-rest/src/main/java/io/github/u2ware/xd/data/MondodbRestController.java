package io.github.u2ware.xd.data;

import java.util.List;
import java.util.Map;

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

    //protected Log logger = LogFactory.getLog(getClass());

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
			q.append("_class", Entity.class.getName());
			
			Query query = new BasicQuery(q);
			Long documentCount = mongoTemplate.count(query, Entity.class, collectionName);
			
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			obj.put("collectionName", collectionName);
			obj.put("documentCount", documentCount);
			content.add(obj);
		}
		return new PageImpl<DBObject>(content);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}")
	public Page<Entity> documents(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			Pageable pageable) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		
		BasicDBObject q = new BasicDBObject();
		q.append("_class", Entity.class.getName());
		Query query = new BasicQuery(q).with(pageable);

		Long count = mongoTemplate.count(query, Entity.class, collectionName);
		List<Entity> content = mongoTemplate.find(query, Entity.class, collectionName);
		return new PageImpl<Entity>(content, pageable, count);
	}

    @RequestMapping(value="/{databaseName}/{collectionName}/{id}")
	public Entity document(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

//		logger.info("document: "+databaseName);
//		logger.info("document: "+collectionName);
//		logger.info("document: "+id);
		
		Entity result = mongoTemplate.findById(id, Entity.class, collectionName);
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

//    private static HttpServletRequest request(){
//    	return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//    }
}
