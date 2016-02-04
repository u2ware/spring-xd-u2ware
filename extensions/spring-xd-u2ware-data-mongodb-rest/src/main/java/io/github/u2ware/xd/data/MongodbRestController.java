package io.github.u2ware.xd.data;

import io.github.u2ware.xd.data.DateTimeUtils.IntervalHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@RestController
public class MongodbRestController {

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
	public List<DBObject> database() throws Exception{
		
    	List<DBObject> content = Lists.newArrayList();
    	
		for(String databaseName : mongo.getDatabaseNames()){

			DB database = mongo.getDB(databaseName);
			Long totalDocumentCount = 0l;
			Set<String> collectionNames = database.getCollectionNames();
			for(String collectionName : collectionNames){
				DBCollection collection = database.getCollection(collectionName);
				totalDocumentCount = totalDocumentCount + collection.count();
			}

			BasicDBObject d = new BasicDBObject();
			d.put("databaseName", databaseName);
			d.put("collectionCount", collectionNames.size());
			d.put("totalDocumentCount", totalDocumentCount);
			content.add(d);
		}
		return content;
	}
    
    @RequestMapping(value="/raw/{databaseName}", method=RequestMethod.GET)
	public List<DBObject> collections(
			@PathVariable("databaseName") String databaseName) throws Exception{

		DB database = mongo.getDB(databaseName);

    	List<DBObject> content = Lists.newArrayList();
		for(String collectionName : database.getCollectionNames()){

			DBCollection collection = database.getCollection(collectionName);
			
			BasicDBObject obj = new BasicDBObject();
			obj.put("collectionName", collectionName);
			obj.put("documentCount", collection.count());
			content.add(obj);
		}
		
		return content;
	}
    @RequestMapping(value="/raw/{databaseName}/{collectionName}", method=RequestMethod.GET)
	public List<Entity> documents(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName) throws Exception{

    	MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		return mongoTemplate.findAll(Entity.class, collectionName);
	}

    @RequestMapping(value="/raw/{databaseName}/{collectionName}/{id}", method=RequestMethod.GET)
	public Entity document(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName, 
			@PathVariable("id") String id) throws Exception{
    	
    	Object key = NumberUtils.isNumber(id) ? NumberUtils.createLong(id) : id ;	
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		Entity result = mongoTemplate.findById(key, Entity.class, collectionName);
		return result;
	}
    
    //////////////////////////////
	// Monitor
	//////////////////////////////
    @RequestMapping(value="/monitor/{entityName}", method=RequestMethod.GET)
	public Page<Entity> monitor(
			@PathVariable("entityName") String entityName, 
			Pageable pageable) throws Exception{
    	
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		
		BasicDBObject q = new BasicDBObject();
		Query query = new BasicQuery(q).with(pageable);

		Long count = mongoTemplate.count(query, Entity.class, entityName);
		List<Entity> content = mongoTemplate.find(query, Entity.class, entityName);
		return new PageImpl<Entity>(content, pageable, count);
	}

    @RequestMapping(value="/monitor/{entityName}/{id}", method=RequestMethod.GET)
	public WebAsyncTask<Entity> monitor(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id) throws Exception{
    	
		Callable<Entity> callable =  new Callable<Entity>() {
			public Entity call() throws Exception {
				
				Thread.sleep(3000);

				MongoTemplate mongoTemplate = getMongoTemplate(entityName);
				Entity current = mongoTemplate.findById(id, Entity.class, entityName);
				return current;
			}};
		return new WebAsyncTask<>(4000, callable);
	}
    
    
    @RequestMapping(value="/monitor/{entityName}/{id}", method=RequestMethod.POST)
	public Entity monitorUpdate(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="name", required=false) String name,
			final @RequestParam(name="criteria", required=false) String criteria,
			final @RequestParam(name="interval", required=false) Long interval) throws Exception{
    
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		Entity current = mongoTemplate.findById(id, Entity.class, entityName);

		if(current != null){			
			if(StringUtils.hasText(name)){
				current.setName("null".equals(name) ? null : name);
			}
			if(StringUtils.hasText(criteria)){
				current.setCriteria("null".equals(criteria) ? null : criteria);
			}
			if(interval != null){
				current.setInterval(interval < 0l ? null : interval);
			}
			mongoTemplate.save(current, entityName);
		}
    	return current;
    }    
    

    //////////////////////////////
	// History
	//////////////////////////////
    @RequestMapping(value="/history/{entityName}/{id}", method=RequestMethod.GET)
	public Page<Entity> history(
			@PathVariable("entityName") String entityName, 
			@PathVariable("id") String id,
			final @RequestParam(name="min", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime min, 
			final @RequestParam(name="max", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime max, 
			final @RequestParam(name="datetime", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime datetime, 
			final @RequestParam(name="interval", required=false) Interval interval,
			Pageable pageable) throws Exception{
		
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);

		DateTime s = null, e = null;
		if(min != null && max != null){
			s = min;
			e = max;
		}else{
			DateTime x = datetime != null ? datetime : DateTime.now();

	    	if(Interval.HOUR.equals(interval)){
				s = DateTimeUtils.minimumHourOfDay(x);
				e = DateTimeUtils.maximumHourOfDay(x);
	    		
	    	}else if(Interval.DAY.equals(interval)){
				s = DateTimeUtils.minimumDayOfMonth(x);
				e = DateTimeUtils.maximumDayOfMonth(x);

	    	}else if(Interval.MONTH.equals(interval)){
				s = DateTimeUtils.minimumMonthOfYear(x);
				e = DateTimeUtils.maximumMonthOfYear(x);
	    	}
		}
		
		Query query = null;
		if(s != null && e != null){
			query = new Query(Criteria.where("_id").gte(s.getMillis()).lte(e.getMillis())).with(pageable);
		}else{
			query = new Query();
		}

		Long count = mongoTemplate.count(query, Entity.class, id);
		List<Entity> content = mongoTemplate.find(query, Entity.class, id);
		return new PageImpl<Entity>(content, pageable, count);
	}
    
    //////////////////////////////
	// Chart
	//////////////////////////////
    public static enum Interval{
    	HOUR, //24
    	DAY, //total day of month
    	MONTH //12
    }
    public static enum Calculation{
    	LAST, MIN, MAX, AVG;
    }
    
    @RequestMapping(value="/chart/{entityName}/{id}", method=RequestMethod.GET)
	public DBObject chart(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="datetime", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime datetime, 
			final @RequestParam(name="interval", required=false, defaultValue="HOUR") Interval interval,
			final @RequestParam(name="calculation", required=false, defaultValue="LAST") Calculation calculation) throws Exception{
    	
    	List<Object> data = chartValue(entityName, id, datetime, interval, calculation);
    	
    	final BasicDBObject result = new BasicDBObject();
    	result.put("chartName", id);
    	result.put("dataCount", data.size());
    	result.put("data", data);
		return result;
    }

    
	private List<Object> chartValue(final String entityName, final String id, final DateTime datetime, final Interval interval, final Calculation calculation) {
		
    	final List<Object> data = Lists.newArrayList();
    	
    	if(Interval.HOUR.equals(interval)){
        	DateTimeUtils.hours(datetime, new IntervalHandler() {
        		private Object value;
        		public void interval(int index, DateTime min, DateTime max) {
    	    		value = chartValue(value, entityName, id, index, min, max, calculation);
    				data.add(value);
    			}
    		});

    	}else if(Interval.DAY.equals(interval)){
        	DateTimeUtils.days(datetime, new IntervalHandler() {
        		private Object value;
        		public void interval(int index, DateTime min, DateTime max) {
    	    		value = chartValue(value, entityName, id, index, min, max, calculation);
    				data.add(value);
    			}
    		});
        	
		}else if(Interval.MONTH.equals(interval)){
	    	DateTimeUtils.months(datetime, new IntervalHandler() {
	    		private Object value;
	    		public void interval(int index, DateTime min, DateTime max) {
		    		value = chartValue(value, entityName, id, index, min, max, calculation);
					data.add(value);
				}
			});
		}
    	return data;
	}
    
    
	private Object chartValue(Object beforeValue, String entityName, String id, int index, DateTime min, DateTime max, Calculation calculation) {
		if(min.isAfterNow()){
			logger.info(index+": "+min+" ~ "+max+": isAfterNow=null");
    		return null;
		}

		MongoTemplate mongoTemplate = getMongoTemplate(entityName);

		AggregationOperation operation1 = TypedAggregation.match(Criteria.where("_id").gte(min.getMillis()).lte(max.getMillis()));
		AggregationOperation operation2 = null;
		
		if(Calculation.LAST.equals(calculation)){
			operation2 = TypedAggregation.group("name").last("value").as("calculation");
			
		}else if(Calculation.AVG.equals(calculation)){
			operation2 = TypedAggregation.group("name").avg("value").as("calculation");
			
		}else if(Calculation.MAX.equals(calculation)){
			operation2 = TypedAggregation.group("name").max("value").as("calculation");
		
		}else if(Calculation.MIN.equals(calculation)){
			operation2 = TypedAggregation.group("name").min("value").as("calculation");
		}
		
		Aggregation aggregation = TypedAggregation.newAggregation(operation1, operation2);	
		AggregationResults<DBObject> result = mongoTemplate.aggregate(aggregation, id, DBObject.class);
		
		DBObject obj = result.getUniqueMappedResult();
//		logger.info(index+" "+aggregation);
//		logger.info(index+" "+obj);
		
		if(obj != null) {
    		logger.info(index+": "+min+" ~ "+max+": calculationValue="+obj.get("calculation"));
			return obj.get("calculation");

		}else{
			if(beforeValue == null){
				
				BasicDBObject q = new BasicDBObject();
				q.append("id", new BasicDBObject("$lt", min.getMillis()));
				Query query = new BasicQuery(q).with(new Sort(Direction.DESC, "id"));

				Entity entity = mongoTemplate.findOne(query, Entity.class, id);
				Object value = (entity != null) ? entity.getValue() : null;
				logger.info(index+": "+min+" ~ "+max+": guessValue="+value);
				return value;

			}else{
				logger.info(index+": "+min+" ~ "+max+": beforeValue="+beforeValue);
				return beforeValue;
			}
		}

		
	}
}
