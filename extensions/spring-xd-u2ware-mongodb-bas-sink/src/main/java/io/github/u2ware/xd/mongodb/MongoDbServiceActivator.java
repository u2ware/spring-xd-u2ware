package io.github.u2ware.xd.mongodb;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoDbServiceActivator implements InitializingBean{

//	private Log logger = LogFactory.getLog(getClass());
	
	private MongoDbFactory mongoDbFactory;
	private MongoTemplate mongoTemplate;
	private MongoConverter mongoConverter;
	private String collectionName;

	public MongoDbServiceActivator(MongoDbFactory mongoDbFactory){
		this.mongoDbFactory = mongoDbFactory;
	}
	
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public void setMongoConverter(MongoConverter mongoConverter) {
		this.mongoConverter = mongoConverter;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		this.mongoTemplate = new MongoTemplate(mongoDbFactory, mongoConverter);
	}

	public void execute(Message<?> request) throws Exception{
		
		try{

//			BuildingAutomationSystemData payload = (BuildingAutomationSystemData)request.getPayload();
//			String id = payload.getId();
//			Object value = payload.getValue();

			Object payload = request.getPayload();
			BeanWrapper wrapper = new BeanWrapperImpl(payload);
			String id = (String)wrapper.getPropertyValue("id");
			Object value = wrapper.getPropertyValue("value");		
			long timestamp = request.getHeaders().get(MessageHeaders.TIMESTAMP, Long.class);

//			logger.debug("payload: "+payload);
//			logger.debug("id: "+id);
//			logger.debug("presentValue: "+presentValue);
//			logger.debug("timestamp: "+timestamp);
//			logger.debug("mongoTemplate: "+mongoTemplate);
//			logger.debug("collectionName: "+collectionName);
			
			DBObject past = mongoTemplate.getCollection(id).find().sort(new BasicDBObject("timestamp", -1)).limit(1).one();
//			logger.debug("past : "+past);
			
			if(past != null){

				Object pastValue = past.get("value");
				
				if(! value.equals(pastValue)){
					
					BasicDBObject objectToSave = new BasicDBObject();
					objectToSave.put("_id", timestamp);
					objectToSave.put("value", value);
					objectToSave.put("timestamp", timestamp);
					objectToSave.put("payload", payload);
					mongoTemplate.save(objectToSave, id);
				}

			}else{
				BasicDBObject objectToSave = new BasicDBObject();
				objectToSave.put("_id", timestamp);
				objectToSave.put("value", value);
				objectToSave.put("timestamp", timestamp);
				objectToSave.put("payload", payload);
				mongoTemplate.save(objectToSave, id);
			}

			BasicDBObject objectToSave = new BasicDBObject();
			objectToSave.put("_id", id);
			objectToSave.put("value", value);
			objectToSave.put("timestamp", timestamp);
			objectToSave.put("payload", payload);
			mongoTemplate.save(objectToSave, collectionName);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}