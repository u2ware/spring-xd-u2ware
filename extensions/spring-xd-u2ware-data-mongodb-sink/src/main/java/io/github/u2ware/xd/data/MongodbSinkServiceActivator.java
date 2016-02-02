package io.github.u2ware.xd.data;

import io.github.u2ware.xd.data.Entity.Strategy;

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
	private boolean valueLogging;
	
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
	public boolean isValueLogging() {
		return valueLogging;
	}
	public void setValueLogging(boolean valueLogging) {
		this.valueLogging = valueLogging;
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
		
//		logger.info("id: "+id);
//		logger.info("value: "+value);
//		logger.info("timestamp: "+timestamp);
//		logger.info("collectionName: "+collectionName);
//		logger.debug("mongoTemplate: "+mongoTemplate);
		

		///////////////////////
		//
		///////////////////////
		Entity entity = mongoTemplate.findById(id, Entity.class, collectionName);
		if(entity != null){
			entity.setValue(value);
			entity.setDatetime(new DateTime(timestamp).toString());
			entity.setPayload(payload);

			//logger.debug("entity update: "+entity);
			
		}else{
			String name = parseValue("name", payload, String.class, "");
			String strategy = parseValue("strategy", payload, String.class, Strategy.NOMAL.toString());
			
			entity = new Entity();
			entity.setId(id);
			entity.setValue(value);
			entity.setDatetime(new DateTime(timestamp).toString());
			entity.setName(name);
			entity.setStrategy(Strategy.valueOf(strategy));
			entity.setPayload(payload);

			//logger.debug("entity create: "+entity);
		}
		mongoTemplate.save(entity, collectionName);
		

		if( !Strategy.NOMAL.equals(entity.getStrategy())){
			
			BasicDBObject q = new BasicDBObject();
			Sort sort = new Sort(Direction.DESC, "id");
			Query query = new BasicQuery(q).with(sort);
			Entity post = mongoTemplate.findOne(query, Entity.class, id.toString());

			Entity objectToSave = new Entity();
			objectToSave.setId(timestamp);
			objectToSave.setValue(value);
			objectToSave.setDatetime(new DateTime(timestamp).toString());
			
			if(post != null){
				
				Object pastValue = post.getValue();
				Long postTimestamp = (Long)post.getId();

				if(Strategy.ALARM.equals(entity.getStrategy())){

					if( ! pastValue.equals(value)){
						//logger.debug("logging alarm: "+objectToSave);
						mongoTemplate.save(objectToSave, id.toString());
					}
					
				}else if(Strategy.HISTORY.equals(entity.getStrategy())){

					if( !pastValue.equals(value) && timestamp - postTimestamp >= 60*60*1000){
						//logger.debug("logging history: "+objectToSave);
						mongoTemplate.save(objectToSave, id.toString());
					}
				}
				
			}else{
				//logger.debug("logging first: "+objectToSave);
				mongoTemplate.save(objectToSave, id.toString());
			}
		}
	}

	

}