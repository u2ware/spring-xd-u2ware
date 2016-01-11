package io.github.u2ware.xd.data;

import io.github.u2ware.xd.data.EntityTimestampSupport.Interval;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@RestController
public class MondodbRestController {

    protected Log logger = LogFactory.getLog(getClass());

	private @Autowired Mongo mongo;
    private Map<String, MongoTemplate> mongoTemplates = Maps.newHashMap();
    
	protected MongoTemplate getMongoTemplate(String databaseName) {
		MongoTemplate mongoTemplate = mongoTemplates.get(databaseName);
		if(mongoTemplate == null){
			mongoTemplate = new MongoTemplate(this.mongo, databaseName);
			mongoTemplates.put(databaseName.toString(), mongoTemplate);
		}
		return mongoTemplate;
	}
//	private static HttpServletRequest request(){
//		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//	}
    
    //////////////////////////////
	// RAW
	//////////////////////////////
    @RequestMapping(value="/raw" , method=RequestMethod.GET)
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
    
    @RequestMapping(value="/raw/{databaseName}", method=RequestMethod.GET)
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

    @RequestMapping(value="/raw/{databaseName}/{collectionName}", method=RequestMethod.GET)
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

    @RequestMapping(value="/raw/{databaseName}/{collectionName}/{id}", method=RequestMethod.GET)
	public Entity document(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

		Entity result = mongoTemplate.findById(id, Entity.class, collectionName);
		return result;
	}

    
    //////////////////////////////
	// DATA, AlARM, CHART
	//////////////////////////////
    @RequestMapping(value="/data/{entityName}/{id}", method=RequestMethod.GET)
	public Entity data(
			@PathVariable("entityName") String entityName, 
			@PathVariable("id") String id) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		Entity result = mongoTemplate.findById(id, Entity.class, entityName);
		return result;
	}
    
    @RequestMapping(value="/alarm/{entityName}/{id}", method=RequestMethod.GET)
	public Callable<Entity> alarm(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id) throws Exception{
    	
    	return new Callable<Entity>() {
			public Entity call() throws Exception {

				MongoTemplate mongoTemplate = getMongoTemplate(entityName);
				Entity current = mongoTemplate.findById(id, Entity.class, entityName);
				
				while(true){
					Thread.sleep(3000);
					Entity alarm = mongoTemplate.findById(id, Entity.class, entityName);
					if(! alarm.getValue().equals(current.getValue())){
						return alarm;
					}
				}
			}};
	}


    @RequestMapping(value="/chart/{entityName}/{id}", method=RequestMethod.GET)
	public DBObject chart(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="datetime", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") DateTime datetime, 
			final @RequestParam(name="interval", required=false, defaultValue="HOUR") Interval interval) throws Exception{
    	
    	final List<Object> data = Lists.newArrayList();
    	
    	List<DateTime> partical = EntityTimestampSupport.getPartical(datetime, interval);
    	
    	for(int i = 0; i < partical.size(); i++){
    		DateTime max = partical.get(i);
    		Object value = chartValue(entityName, id, i, max);
			data.add(value);
    	}

    	BasicDBObject obj = new BasicDBObject();
		obj.put("chartName", id);
		obj.put("startTimestamp", partical.get(0).getMillis());
		obj.put("endTimestamp", partical.get(partical.size()-1).getMillis());
		obj.put("dataCount", data.size());
		obj.put("data", data);
		return obj;
    }

	private Object chartValue(String entityName, String id, int index, DateTime datetime) {
		if(datetime.isAfterNow()){
    		logger.info(index+" ~"+datetime+": isAfterNow");
    		return null;
		}

		MongoTemplate mongoTemplate = getMongoTemplate(entityName);

		BasicDBObject q = new BasicDBObject();
		q.append("id", new BasicDBObject("$lt", datetime.getMillis()));
		Query query = new BasicQuery(q).with(new Sort(Direction.DESC, "id"));

		Entity entity = mongoTemplate.findOne(query, Entity.class, id);
    	Object value = (entity != null) ? entity.getValue() : null;

		logger.info(index+": ~"+datetime+" = "+value);
		return value;
	}
}
