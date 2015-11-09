package io.github.u2ware.xd.mongodb;

import io.github.u2ware.integration.netty.support.NettyHeaders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDbServiceActivator {

	//private Log logger = LogFactory.getLog(getClass());
	
	private Mongo mongo;
	private MongoConverter mongoConverter;
	private Map<String, MongoTemplate> mongoTemplates;

	public MongoDbServiceActivator(Mongo mongo){
		this.mongo = mongo;
		this.mongoTemplates = Maps.newHashMap();
	}
	
	public void setMongoConverter(MongoConverter mongoConverter) {
		this.mongoConverter = mongoConverter;
	}

	@SuppressWarnings("deprecation")
	protected MongoTemplate getMongoTemplate(String databaseName) {
		MongoTemplate mongoTemplate = mongoTemplates.get(databaseName);
		if(mongoTemplate == null){
			mongoTemplate = new MongoTemplate(new  SimpleMongoDbFactory(this.mongo, databaseName), this.mongoConverter);
			mongoTemplates.put(databaseName.toString(), mongoTemplate);
		}
		return mongoTemplate;
	}
	
	public Message<?> execute(Message<?> request) throws Exception{

		Object payload = null;
		try{
			String requestMethod = request.getHeaders().get(NettyHeaders.HTTP_REQUEST_METHOD).toString();
			String requestPath = request.getHeaders().get(NettyHeaders.HTTP_REQUEST_PATH).toString();
			String query = request.getPayload().toString();
			
			String queryComponent = StringUtils.hasText(query) ? query : null;
			List<String> requestPathComponents = Lists.newArrayList();
			String[] paths = StringUtils.delimitedListToStringArray(requestPath, "/");
			for(String path: paths){
				if(StringUtils.hasText(path)){
					requestPathComponents.add(path);
				}
			}
			
			if("POST".equals(requestMethod)){
				if(requestPathComponents.size() >= 0){
					payload = read(request, requestPathComponents, queryComponent);
				}
			}else if("PUT".equals(requestMethod)){
				if(requestPathComponents.size() > 0){
					payload = create(request, requestPathComponents);
				}
			}
		}catch(Exception e){
			return null;
		}
		
		if(payload != null){
			Message<?> response = MessageBuilder.withPayload(payload).build();
			return response;
		}
		return null;
	}
	
	public Object read(Message<?> request, List<String> paths, String query) throws Exception{
		
		//logger.debug("request: "+paths);
		
		if(paths.size() == 0){
			
			Set<DBObject> results = Sets.newHashSet();
			for(String databaseName : mongo.getDatabaseNames()){
				BasicDBObject obj = new BasicDBObject();
				obj.put("databaseName", databaseName);
				results.add(obj);
			}
			if (!CollectionUtils.isEmpty(results)){
				return results;
			}
	
		}else if(paths.size() == 1){
			
			String databaseName = paths.get(0);
			MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

			Set<DBObject> results = Sets.newHashSet();
			for(String collectionName : mongoTemplate.getCollectionNames()){
				BasicDBObject obj = new BasicDBObject();
				obj.put("databaseName", databaseName);
				obj.put("collectionName", collectionName);
				results.add(obj);
			}
			if (!CollectionUtils.isEmpty(results)){
				return results;
			}
			
		}else if(paths.size() == 2){
			
			String databaseName = paths.get(0);
			String collectionName = paths.get(1);
			MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

			if(StringUtils.hasText(query) ){

				List<?> results = mongoTemplate.find(new BasicQuery(query), DBObject.class, collectionName);
				if (!CollectionUtils.isEmpty(results)){
					return results;
				}
				
			}else{
				List<?> results = mongoTemplate.findAll(DBObject.class, collectionName);
				if (!CollectionUtils.isEmpty(results)){
					return results;
				}
			}
			
		}else if(paths.size() == 3){

			String databaseName = paths.get(0);
			String collectionName = paths.get(1);
			String id = paths.get(2);
			MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

			DBObject result = mongoTemplate.findById(id, DBObject.class, collectionName);
			if (result != null){
				return result;
			}
		}
		return null;
	}
	
	public Object create(Message<?> request, List<String> paths) throws Exception{
		if(paths.size() == 1){
			return null;
			
		}else if(paths.size() == 2){
			String databaseName = paths.get(0);
			String collectionName = paths.get(1);
			MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

			DBCollection collection = mongoTemplate.createCollection(collectionName);
			if(collection != null){
				BasicDBObject result = new BasicDBObject();
				result.put("databaseName", databaseName);
				result.put("collectionName", collectionName);
				return result ;
			}
		}
		return null;
	}
}
