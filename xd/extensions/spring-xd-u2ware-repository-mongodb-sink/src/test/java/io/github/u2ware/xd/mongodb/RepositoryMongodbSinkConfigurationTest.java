package io.github.u2ware.xd.mongodb;

import io.github.u2ware.xd.mongodb.MongodbServer.Sample;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("use_json_input")
public class RepositoryMongodbSinkConfigurationTest {

//	protected static MongoClient mongoClient;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		MongodbServer.startup(27017);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		MongodbServer.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());


    @Autowired @Qualifier("input")
    MessageChannel input;
    
	@Test
	public void test() throws Exception{

		
		input.send(MessageBuilder.withPayload(new Sample("Mina", 19, "aaa")).build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload(new Sample("Mina", 22, "bbb")).build());
		Thread.sleep(1000);
		
		input.send(MessageBuilder.withPayload(new Sample("Mina", 19, "ccc")).build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload(new Sample("Mina", 19, "ddd")).build());
		Thread.sleep(1000);

//		Tuple tuple = TupleBuilder.fromString("{\"id\":\"Mina\",\"value\":\"cccc\"}");
//		logger.debug(tuple);
		
		//ObjectMapper mapper = new ObjectMapper();
		//JsonNode root = mapper.readTree("{\"key\":\"Mina\",\"value\":\"cccc\"}");
		//logger.debug(root);

//		BasicDBObject dbo = new BasicDBObject();
//		dbo.put("key", "Mina");
//		dbo.put("value", "cccc");
//		logger.debug(dbo);
		
		
//		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\", \"value\":\"xxxx\"}").build());
//		Thread.sleep(1000);

		
		
		
		
		
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoTemplate template = new MongoTemplate(mongoClient, "MyDatabase");
		List<DBObject> r = null;
		
		
		
		r= template.findAll(DBObject.class, "MyDatabase");
		//Assert.assertEquals(1, r.size());
		logger.debug("\tMyDatabase");
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}

		r = template.findAll(DBObject.class, "Mina");
		logger.debug("\tMina");
		//Assert.assertEquals(3, r.size());
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}
	}
}


