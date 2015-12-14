package io.github.u2ware.xd.ibs.controller;

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
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongodbRestServiceActivator {

	//private Log logger = LogFactory.getLog(getClass());
	
	private Mongo mongo;
	private MongoConverter mongoConverter;
	private Map<String, MongoTemplate> mongoTemplates;

	public MongodbRestServiceActivator(Mongo mongo){
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

		String query = request.getPayload().toString();
		String requestPath = request.getHeaders().get(NettyHeaders.HTTP_REQUEST_PATH).toString();

		String[] requestPathComponents = StringUtils.delimitedListToStringArray(requestPath, "/");
		List<String> paths = Lists.newArrayList();
		for(String p: requestPathComponents){
			if(StringUtils.hasText(p)){
				paths.add(p);
			}
		}
//		logger.debug(requestPath);
//		logger.debug(paths);
		
		if(paths == null || paths.size() == 0){
			payload = database();

		}else if(paths.size() == 1){
			payload = collections(paths.get(0));
		
		}else if(paths.size() == 2 && ! StringUtils.hasText(query)){
			payload = entities(paths.get(0), paths.get(1), null);

		}else if(paths.size() == 2 && StringUtils.hasText(query)){
			payload = entities(paths.get(0), paths.get(1), query);

		}else if(paths.size() == 3){
			payload = entity(paths.get(0), paths.get(1), paths.get(2));
		}
		
		if(payload != null){
			Message<?> response = MessageBuilder.withPayload(payload).build();
			return response;
		}
		return null;
	}

	public Object database() throws Exception{
		Set<DBObject> results = Sets.newHashSet();
		for(String databaseName : mongo.getDatabaseNames()){
			BasicDBObject obj = new BasicDBObject();
			obj.put("databaseName", databaseName);
			results.add(obj);
		}
		if (!CollectionUtils.isEmpty(results)){
			return results;
		}
		return null;
	}

	public Object collections(String databaseName) throws Exception{

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
		return null;
	}

	public Object entities(String databaseName, String collectionName, String query) throws Exception{

		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

		List<?> results = null;
		if(StringUtils.hasText(query)){
			results = mongoTemplate.find(new BasicQuery(query), DBObject.class, collectionName);
		}else{
			results = mongoTemplate.findAll(DBObject.class, collectionName);
		}
		if (!CollectionUtils.isEmpty(results)){
			return results;
		}
		return null;
	}
	
	public Object entity(String databaseName, String collectionName, String id) throws Exception{
		MongoTemplate mongoTemplate = getMongoTemplate(databaseName);

		DBObject result = mongoTemplate.findById(id, DBObject.class, collectionName);
		if (result != null){
			return result;
		}
		return null;
	}
}
