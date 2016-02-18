package io.github.u2ware.xd.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private Log logger = LogFactory.getLog(getClass());

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
		Entity current = mongoTemplate.findById(id, Entity.class, collectionName);
		if(current == null){
			String name = parseValue("name", payload, String.class, null);
			String criteria = parseValue("criteria", payload, String.class, null);
			Long interval = parseValue("interval", payload, Long.class, null);
			
			current = new Entity();
			current.setId(id);
			current.setValue(value);
			current.setDatetime(datetime);
			current.setName(name);
			current.setCriteria(criteria);
			current.setInterval(interval);
			current.setStatus(payload);
		}		
		
		Query query = new BasicQuery(new BasicDBObject()).with(new Sort(Direction.DESC, "id"));
		Entity before = mongoTemplate.findOne(query, Entity.class, id.toString());
		if(before == null){
			before = new Entity();
		}
		
		Entity after = new Entity();
		after.setId(timestamp);
		after.setValue(value);
		after.setDatetime(datetime);
		after.setName(id.toString());
		after.setInterval(current.getInterval());
		after.setCriteria(current.getCriteria());
		
		if(current.getInterval() != null){			
			Object beforeValue = before.getValue();
					
			if(! value.equals(beforeValue)){

				Long beforeTimestamp = (Long)before.getId();
				if(beforeTimestamp == null || timestamp - beforeTimestamp >= current.getInterval()){
					after.setStatus("interval");
					logger.info("interval: "+after);
					mongoTemplate.save(after, id.toString());
				}
			}
		}
		if( current.getCriteria() != null ){
			
			Object currentValue = current.getValue();

			if(! value.equals(currentValue)){
				
				if(parseValue(current.getCriteria(), after, Boolean.class, false)){
					after.setStatus("criteria");
					logger.info("criteria: "+after);
					mongoTemplate.save(after, id.toString());
				}
			}
		}
				
		current.setValue(value);
		current.setDatetime(datetime);
		current.setStatus(after.getStatus());
		mongoTemplate.save(current, collectionName);
	}
}