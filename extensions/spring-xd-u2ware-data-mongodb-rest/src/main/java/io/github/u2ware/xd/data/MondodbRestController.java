package io.github.u2ware.xd.data;

import io.github.u2ware.xd.data.EntityQuerySupport.CalculationType;
import io.github.u2ware.xd.data.EntityQuerySupport.DatetimeIntervalHandler;
import io.github.u2ware.xd.data.EntityQuerySupport.IntervalType;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    private EntityQuerySupport timestampSupport = new EntityQuerySupport();
    
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
	// DATA and CHART
	//////////////////////////////
    @RequestMapping(value="/data/{entityName}", method=RequestMethod.GET)
	public Page<Entity> entities(
			@PathVariable("entityName") String entityName, 
			Pageable pageable) throws Exception{
		
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		
		BasicDBObject q = new BasicDBObject();
		q.append("_class", Entity.class.getName());
		Query query = new BasicQuery(q).with(pageable);

		Long count = mongoTemplate.count(query, Entity.class, entityName);
		List<Entity> content = mongoTemplate.find(query, Entity.class, entityName);
		return new PageImpl<Entity>(content, pageable, count);
	}
    
    @RequestMapping(value="/data/{entityName}/{id}", method=RequestMethod.GET)
	public Entity entity(
			@PathVariable("entityName") String entityName, 
			@PathVariable("id") String id) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		Entity result = mongoTemplate.findById(id, Entity.class, entityName);
		return result;
	}
    
    @RequestMapping(value="/data/{entityName}/{id}", method=RequestMethod.POST)
	public Entity entityAsyn(
			@PathVariable("entityName") String entityName, 
			@PathVariable("id") String id) throws Exception{

		return null;
	}

    @RequestMapping(value="/chart/{entityName}/{id}", method=RequestMethod.GET)
	public DBObject chart(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="datetime", required=false) DateTime datetime, 
			final @RequestParam(name="interval", required=false, defaultValue="REALTIME") IntervalType interval, 
			final @RequestParam(name="calculation", required=false, defaultValue="AVG") CalculationType calculation) throws Exception{
    	
    	final List<Object> data = Lists.newArrayList();
    	
		timestampSupport.handle(interval, datetime, new DatetimeIntervalHandler() {

	    	private Object value = null;
			public void interval(int index, DateTime min, DateTime max) {
				value = chartValue(value, entityName, id, calculation, index, min, max);
				data.add(value);
			}
		});
		
		BasicDBObject obj = new BasicDBObject();
		obj.put("chartName", id);
		obj.put("dataCount", data.size());
		obj.put("data", data);
		return obj;
	}
    
    
    private Object chartValue(Object beforeValue, String entityName, String id, CalculationType calculation, int index, DateTime min, DateTime max){

		if(min.isAfterNow()){
    		logger.info(index+" "+min+" ~ "+max+": overtime=null");
    		return null;
		}
    	
    	MongoTemplate mongoTemplate = getMongoTemplate(entityName);

		AggregationOperation operation1 = TypedAggregation.match(Criteria.where("timestamp").gte(min.getMillis()).lte(max.getMillis()));
		AggregationOperation operation2 = null;

		if(CalculationType.AVG.equals(calculation)){
			operation2 = TypedAggregation.group("payload").avg("value").as("calculation");
			
		}else if(CalculationType.MAX.equals(calculation)){
			operation2 = TypedAggregation.group("payload").max("value").as("calculation");
		
		}else if(CalculationType.MIN.equals(calculation)){
			operation2 = TypedAggregation.group("payload").min("value").as("calculation");
		
		}else{
    		logger.info(index+" "+min+" ~ "+max+": calculation error="+calculation);
			return null;
		}
		
		Aggregation aggregation = TypedAggregation.newAggregation(operation1, operation2);	
		AggregationResults<DBObject> result = mongoTemplate.aggregate(aggregation, id, DBObject.class);
		
		DBObject obj = result.getUniqueMappedResult();
		if(obj != null) {
    		logger.info(index+" "+min+" ~ "+max+": calculation="+obj.get("calculation"));
			return obj.get("calculation");
		}
		
		logger.info(index+" "+min+" ~ "+max+": before="+beforeValue);
		return beforeValue;
    }
}
