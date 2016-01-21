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
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.mongodb.BasicDBObject;

public class MongodbSinkServiceActivator implements InitializingBean, BeanFactoryAware{

	//private Log logger = LogFactory.getLog(getClass());

	private BeanFactory beanFactory;
	private volatile StandardEvaluationContext evaluationContext;
	private volatile Expression idExpression;
	private volatile Expression valueExpression;
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
	public Expression getIdExpression() {
		return idExpression;
	}
	public void setIdExpression(Expression idExpression) {
		this.idExpression = idExpression;
	}
	public Expression getValueExpression() {
		return valueExpression;
	}
	public void setValueExpression(Expression valueExpression) {
		this.valueExpression = valueExpression;
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
		this.evaluationContext = ExpressionUtils.createStandardEvaluationContext(this.beanFactory);
		this.mongoTemplate = new MongoTemplate(mongoDbFactory, mongoConverter);
	}

	public void execute(Message<?> requestMessage) throws Exception{
		
//		Object payload = requestMessage.getPayload();
//		logger.info(requestMessage);
//		logger.info(requestMessage.getHeaders());
//		logger.info(payload.getClass());
//		logger.info(payload);
		
		Object id = this.idExpression != null 
				? this.idExpression.getValue(this.evaluationContext, requestMessage, Object.class)
				: null;
		
		Object value = this.valueExpression != null 
				? this.valueExpression.getValue(this.evaluationContext, requestMessage, Object.class)
				: null;
		long timestamp = requestMessage.getHeaders().get(MessageHeaders.TIMESTAMP, Long.class);

		if(id == null || value == null) return;
				
		id = collectionName+"_"+id;

//		logger.info("id: "+id);
//		logger.info("value: "+value);
//		logger.info("timestamp: "+timestamp);
//		logger.info("collectionName: "+collectionName);
//		logger.debug("mongoTemplate: "+mongoTemplate);
		
		
		if(valueLogging){
			
			BasicDBObject q = new BasicDBObject();
			Sort sort = new Sort(Direction.DESC, "id");
			Query query = new BasicQuery(q).with(sort);
			
			
			//DBObject past = mongoTemplate.getCollection(id.toString()).find().sort(new BasicDBObject("timestamp", -1)).limit(1).one();
			Entity post = mongoTemplate.findOne(query, Entity.class, id.toString());
			
//			logger.debug("past : "+post);
			
			boolean history = false;
			if(post != null){
				Object pastValue = post.getValue();
				if(! value.equals(pastValue)){
					history = true;
				}
			}else{
				history = true;
			}

			if(history){
//				BasicDBObject objectToSave = new BasicDBObject();
//				objectToSave.put("_id", timestamp);
//				objectToSave.put("value", value);
//				objectToSave.put("usage", "history");
//				objectToSave.put("timestamp", timestamp);
//				objectToSave.put("payload", payload);
				Entity objectToSave = new Entity();
				objectToSave.setId(timestamp);
				objectToSave.setValue(value);
				objectToSave.setDatetime(new DateTime(timestamp).toString());
				objectToSave.setPayload(id.toString());

				mongoTemplate.save(objectToSave, id.toString());
				//logger.info("save: "+timestamp+" in "+id);
			}
		}
		
//		BasicDBObject objectToSave = new BasicDBObject();
//		objectToSave.put("_id", id);
//		objectToSave.put("value", value);
//		objectToSave.put("usage", "current");
//		objectToSave.put("timestamp", timestamp);
//		objectToSave.put("payload", payload);

		Entity objectToSave = new Entity();
		objectToSave.setId(id);
		objectToSave.setValue(value);
		objectToSave.setDatetime(new DateTime(timestamp).toString());
		objectToSave.setPayload(collectionName);

		mongoTemplate.save(objectToSave, collectionName);
		//logger.info("save: "+id+" in "+collectionName);
	}

	

}