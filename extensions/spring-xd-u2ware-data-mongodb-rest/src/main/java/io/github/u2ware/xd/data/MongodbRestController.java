package io.github.u2ware.xd.data;

import io.github.u2ware.xd.data.support.DateTimeUtils;
import io.github.u2ware.xd.data.support.DateTimeUtils.IntervalHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

@Controller
public class MongodbRestController {

    //protected Log logger = LogFactory.getLog(getClass());

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

//	private HttpServletRequest request(){
//		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//	}

    
    
    //////////////////////////////
	// RAW
	//////////////////////////////
    @RequestMapping(value="/raw" , method=RequestMethod.GET)
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
	public List<Entity> documents(
			@PathVariable("databaseName") String databaseName, 
			@PathVariable("collectionName") String collectionName) throws Exception{

    	MongoTemplate mongoTemplate = getMongoTemplate(databaseName);
		return mongoTemplate.findAll(Entity.class, collectionName);
	}

    @RequestMapping(value="/raw/{databaseName}/{collectionName}/{id}", method=RequestMethod.GET)
    @ResponseBody
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
	// Setting
	//////////////////////////////
    @RequestMapping(value="/setting/{entityName}", method=RequestMethod.GET)
	public ModelAndView setting(
			final @PathVariable("entityName") String entityName) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		final List<Entity> entities = mongoTemplate.findAll(Entity.class, entityName);
    	
    	return new ModelAndView(new AbstractXlsxView(){

    		protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

    			Sheet sheet = workbook.createSheet(entityName);
    			Row row = sheet.createRow(0);
    			
    			row.createCell(0).setCellValue("id");
    			row.createCell(1).setCellValue("name");
    			row.createCell(2).setCellValue("criteria");
    			row.createCell(3).setCellValue("interval");
    			
    			int r = 1;
    			for(Entity entity : entities){

    				row = sheet.createRow(r);
    				
    				row.createCell(0).setCellValue(entity.getId().toString());
    				if(entity.getName() != null)
    					row.createCell(1).setCellValue(entity.getName().toString());
    				if(entity.getCriteria() != null)
    					row.createCell(2).setCellValue(entity.getCriteria().toString());
    				if(entity.getInterval() != null)
    					row.createCell(3).setCellValue(entity.getInterval().toString());

    				r++;
    			}
			}
    	});
    }
    
    @RequestMapping(value="/setting/{entityName}", method=RequestMethod.POST)
    @ResponseBody
	public Map<String,Object> setting(
			final @PathVariable("entityName") String entityName,
			final @RequestParam("file") MultipartFile file) throws Exception{
    
    	Map<String, Object> result = Maps.newHashMap();
    	int success = 0;
    	int failure = 0;
    	List<String> errors = Lists.newArrayList();
    	
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
    	
		try{
	    	XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
	    	Sheet sheet = workbook.getSheetAt(0);
	    	
	    	for(int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++){
	    		
	    		Row row = sheet.getRow(r);
	    		
	    		String id       = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null;
	    		String name     = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null;
	    		String criteria = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null;
	    		String interval = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null;

	    		if(StringUtils.hasText(id)){
	    			
	    			Entity current = mongoTemplate.findById(id, Entity.class, entityName);
	    			if(current != null){
					
	    				try{
	        				current.setName(   StringUtils.hasText(name) ? name : null);
	        				current.setCriteria( StringUtils.hasText(criteria) ? name : null );
	        				current.setInterval( NumberUtils.isNumber(interval)? NumberUtils.toLong(interval) : null);
	        				mongoTemplate.save(current, entityName);

	        				success++;
	    				}catch(Exception e){
	            			failure++;
	            			errors.add(r+": "+e.getMessage());
	    				}
	    			}else{
	        			failure++;
	        			errors.add(r+": entity not found");
	    			}
	    		}else{
	    			failure++;
	    			errors.add(r+": entity not found");
	    		}
	    	}
		}catch(Exception e){
			errors.add(e.getMessage());
		}
		
    	result.put("success", success);
    	result.put("failure", failure);
    	result.put("errors", errors);
		return result;
    }

    @RequestMapping(value="/setting/{entityName}/{id}", method=RequestMethod.POST)
    @ResponseBody
	public Entity setting(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="name", required=false) String name,
			final @RequestParam(name="criteria", required=false) String criteria,
			final @RequestParam(name="interval", required=false) Long interval) throws Exception{
    
		MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		Entity current = mongoTemplate.findById(id, Entity.class, entityName);

		if(current != null){
			current.setName(     (name == null     || "null".equals(name)  ) ? null : name);
			current.setCriteria( (criteria == null || "null".equals(criteria) ) ? null : criteria);
			current.setInterval( (interval == null || interval < 0l) ? null : interval);
			mongoTemplate.save(current, entityName);
		}
    	return current;
    }    
    
    //////////////////////////////
	// current
	//////////////////////////////
    @RequestMapping(value="/current/{entityName}", method=RequestMethod.GET)
    @ResponseBody
	public Page<Entity> current(
			@PathVariable("entityName") String entityName, 
			@RequestParam(name="status", required=false) String status,
			@RequestParam(name="name", required=false) String name,
			Pageable pageable) throws Exception{

    	MongoTemplate mongoTemplate = getMongoTemplate(entityName);

		Criteria statusCriteria = null;
		if("criteria".equals(status)){
			statusCriteria = Criteria.where("criteria").ne(null);	
		}else if("interval".equals(status)){
			statusCriteria = Criteria.where("interval").gt(new Long(0));
		}else if("status".equals(status)){
			statusCriteria = Criteria.where("status").is("criteria");
		}

		Criteria nameCriteria = null; 
		if(StringUtils.hasText(name)){
			Criteria criteria1 = Criteria.where("name").regex(name);
			Criteria criteria2 = Criteria.where("id").regex(name);
			nameCriteria = new Criteria().orOperator(criteria1, criteria2);
		}

		Criteria criteria = null;
		if(nameCriteria != null && statusCriteria != null){
			criteria = new Criteria().andOperator(statusCriteria, nameCriteria);
		}else if(statusCriteria != null){
			criteria = statusCriteria;
		}else if(nameCriteria != null){
			criteria = nameCriteria;
		}
		
		Query query = (criteria !=null) ? new Query(criteria): new Query();
		query.with(pageable);

		Long count = mongoTemplate.count(query, Entity.class, entityName);
		List<Entity> content = mongoTemplate.find(query, Entity.class, entityName);
		return new PageImpl<Entity>(content, pageable, count);
	}

    @RequestMapping(value="/current/{entityName}/{id}", method=RequestMethod.GET)
    @ResponseBody
	public Entity current(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id) throws Exception{

    	MongoTemplate mongoTemplate = getMongoTemplate(entityName);
		Entity current = mongoTemplate.findById(id, Entity.class, entityName);
		return current;
	}
    
/*
	public Callable<Entity> current(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id) throws Exception{

    	final long hashCode = request().hashCode();
		Callable<Entity> callable =  new Callable<Entity>() {
			public Entity call() throws Exception {
				logger.debug("current started: "+hashCode);
				Thread.sleep(1000);
				MongoTemplate mongoTemplate = getMongoTemplate(entityName);
				Entity current = mongoTemplate.findById(id, Entity.class, entityName);
				logger.debug("current finished: "+hashCode);
				return current;
			}};
		return callable;
	}
*/    

    //////////////////////////////
	// record
	//////////////////////////////
    @RequestMapping(value="/record/{entityName}/{id}", method=RequestMethod.GET)
    @ResponseBody
	public Page<Entity> record(
			@PathVariable("entityName") String entityName, 
			@PathVariable("id") String id,
			final @RequestParam(name="min", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime min, 
			final @RequestParam(name="max", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime max, 
			final @RequestParam(name="datetime", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime datetime, 
			final @RequestParam(name="interval", required=false) Interval interval,
			final @RequestParam(name="status", required=false) String status,
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
		
		Criteria criteria = null;
		if(s != null && e != null){	
			if(status != null){
				criteria = Criteria.where("_id").gte(s.getMillis()).lte(e.getMillis()).and("status").is(status);
			}else{
				criteria = Criteria.where("_id").gte(s.getMillis()).lte(e.getMillis());
			}
		}else{
			if(status != null){
				criteria = Criteria.where("status").is(status);
			}
		}

		Query query = criteria == null ? new Query() : new Query(criteria);
		query.with(pageable);
		
		Long count = mongoTemplate.count(query, Entity.class, id);
		List<Entity> content = mongoTemplate.find(query, Entity.class, id);
		return new PageImpl<Entity>(content, pageable, count);
	}
    
    //////////////////////////////
	// recordChart
	//////////////////////////////
    public static enum Interval{
    	HOUR, //24
    	DAY, //total day of month
    	MONTH //12
    }
    public static enum Calculation{
    	LAST, MIN, MAX, AVG;
    }
    
    @RequestMapping(value="/recordChart/{entityName}/{id}", method=RequestMethod.GET)
    @ResponseBody
	public DBObject recordChart(
			final @PathVariable("entityName") String entityName, 
			final @PathVariable("id") String id,
			final @RequestParam(name="datetime", required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") DateTime datetime, 
			final @RequestParam(name="interval", required=false, defaultValue="HOUR") Interval interval,
			final @RequestParam(name="calculation", required=false, defaultValue="LAST") Calculation calculation) throws Exception{
    	
		DateTime x = datetime != null ? datetime : DateTime.now();
    	List<Object> data = recordChartValue(entityName, id, x, interval, calculation);
    	
    	final BasicDBObject result = new BasicDBObject();
    	result.put("chartName", id);
    	result.put("dataCount", data.size());
    	result.put("data", data);
		return result;
    }

    
	private List<Object> recordChartValue(final String entityName, final String id, final DateTime datetime, final Interval interval, final Calculation calculation) {
		
    	final List<Object> data = Lists.newArrayList();
    	
    	if(Interval.HOUR.equals(interval)){
        	DateTimeUtils.hours(datetime, new IntervalHandler() {
        		private Object value;
        		public void interval(int index, DateTime min, DateTime max) {
    	    		value = recordChartValue(value, entityName, id, index, min, max, calculation);
    				data.add(value);
    			}
    		});

    	}else if(Interval.DAY.equals(interval)){
        	DateTimeUtils.days(datetime, new IntervalHandler() {
        		private Object value;
        		public void interval(int index, DateTime min, DateTime max) {
    	    		value = recordChartValue(value, entityName, id, index, min, max, calculation);
    				data.add(value);
    			}
    		});
        	
		}else if(Interval.MONTH.equals(interval)){
	    	DateTimeUtils.months(datetime, new IntervalHandler() {
	    		private Object value;
	    		public void interval(int index, DateTime min, DateTime max) {
		    		value = recordChartValue(value, entityName, id, index, min, max, calculation);
					data.add(value);
				}
			});
		}
    	return data;
	}
    
    
	private Object recordChartValue(Object beforeValue, String entityName, String id, int index, DateTime min, DateTime max, Calculation calculation) {
		if(min.isAfterNow()){
			//logger.info(index+": "+min+" ~ "+max+": isAfterNow=null");
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
    		//logger.info(index+": "+min+" ~ "+max+": calculationValue="+obj.get("calculation"));
			return obj.get("calculation");

		}else{
			if(beforeValue == null){
				
				BasicDBObject q = new BasicDBObject();
				q.append("id", new BasicDBObject("$lt", min.getMillis()));
				Query query = new BasicQuery(q).with(new Sort(Direction.DESC, "id"));

				Entity entity = mongoTemplate.findOne(query, Entity.class, id);
				Object value = (entity != null) ? entity.getValue() : null;
				//logger.info(index+": "+min+" ~ "+max+": guessValue="+value);
				return value;

			}else{
				//logger.info(index+": "+min+" ~ "+max+": beforeValue="+beforeValue);
				return beforeValue;
			}
		}
	}
}
