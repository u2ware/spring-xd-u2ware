package io.github.u2ware.xd.data;

import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.mongodb.BasicDBObject;

public class MongodbSinkServiceActivator implements InitializingBean, BeanFactoryAware{

	//private Log logger = LogFactory.getLog(getClass());

	private BeanFactory beanFactory;
	private volatile StandardEvaluationContext evaluationContext;
	private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	//private volatile Expression idExpression;
	//private volatile Expression valueExpression;
	//private boolean valueLogging;
	
	private MongoDbFactory mongoDbFactory;
	private MongoTemplate mongoTemplate;
	private MongoConverter mongoConverter;
	private String collectionName;

	public MongodbSinkServiceActivator(MongoDbFactory mongoDbFactory){
		this.mongoDbFactory = mongoDbFactory;
	}
	
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public void setMongoConverter(MongoConverter mongoConverter) {
		this.mongoConverter = mongoConverter;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.mongoTemplate = new MongoTemplate(mongoDbFactory, mongoConverter);
		this.evaluationContext = ExpressionUtils.createStandardEvaluationContext(this.beanFactory);
	}

	private <T> T parseValue(String name, Object rootObject, Class<T> returnType){
		return spelExpressionParser.parseExpression(name).getValue(this.evaluationContext, rootObject, returnType);
	}
	private <T> T parseValue(String name, Object rootObject, Class<T> returnType, T defaultValue){

		try{
			return spelExpressionParser.parseExpression(name).getValue(this.evaluationContext, rootObject, returnType);
			
		}catch(Exception e){
			//e.printStackTrace();
			return defaultValue;
		}
	}
	
	public void execute(Message<?> requestMessage) throws Exception{
		
		Object payload = requestMessage.getPayload();
//		logger.info(requestMessage);
//		logger.info(requestMessage.getHeaders());
//		logger.info(payload.getClass());
//		logger.info(payload);
		
//		Object id = this.idExpression != null 
//				? this.idExpression.getValue(this.evaluationContext, requestMessage, Object.class)
//				: null;
		
//		Object value = this.valueExpression != null 
//				? this.valueExpression.getValue(this.evaluationContext, requestMessage, Object.class)
//				: null;
		Object id = parseValue("id", payload, Object.class);
		Object value = parseValue("value", payload, Object.class);
		

		if(id == null || value == null) return;

		long timestamp = requestMessage.getHeaders().get(MessageHeaders.TIMESTAMP, Long.class);
		String datetime = new DateTime(timestamp).toString();
//		logger.info("id: "+id);
//		logger.info("value: "+value);
//		logger.info("timestamp: "+timestamp);
//		logger.info("collectionName: "+collectionName);
//		logger.debug("mongoTemplate: "+mongoTemplate);
		

		///////////////////////
		//
		///////////////////////
		Entity entity = mongoTemplate.findById(id, Entity.class, collectionName);
		if(entity == null){
			String name = parseValue("name", payload, String.class, null);
			String criteria = parseValue("criteria", payload, String.class, null);
			Long interval = parseValue("interval", payload, Long.class, null);
			
			entity = new Entity();
			entity.setId(id);
			entity.setValue(value);
			entity.setDatetime(datetime);
			entity.setName(name);
			entity.setCriteria(criteria);
			entity.setInterval(interval);
			entity.setPayload(payload);
			
		}else{
			entity.setValue(value);
			entity.setDatetime(datetime);
			entity.setPayload(payload);
		}
		
		Entity record = new Entity();
		record.setId(timestamp);
		record.setValue(value);
		record.setDatetime(datetime);
		record.setName(id.toString());
		
		boolean isRecord = false;

		if( entity.getCriteria() != null ){
			if(parseValue(entity.getCriteria(), record, Boolean.class, false)){
				record.setPayload("criteria");
				isRecord = true;
				//logger.debug("record {"+entity.getCriteria()+"}: "+record);
			}
		}	
		
		if( isRecord == false && entity.getInterval() != null){			
			
			Query query = new BasicQuery(new BasicDBObject()).with(new Sort(Direction.DESC, "id"));
			Entity beforeRecord = mongoTemplate.findOne(query, Entity.class, id.toString());
			
			if(beforeRecord != null){

				if(! beforeRecord.getValue().equals(value)){

					Long beforeRecordTimestamp = (Long)beforeRecord.getId();
					if(timestamp - beforeRecordTimestamp >= entity.getInterval()){
						//logger.debug("record {"+entity.getInterval()+"ms}: "+record);
						record.setPayload("interval");
						isRecord = true;
					}
				}
			}else{
				//logger.debug("record {"+entity.getInterval()+"ms}: "+record);
				record.setPayload("interval");
				isRecord = true;
			}
		}
		
		mongoTemplate.save(entity, collectionName);
		
		if(isRecord){
			mongoTemplate.save(record, id.toString());
		}else{
			//logger.debug("record ignore: "+record);
		}
	}
}