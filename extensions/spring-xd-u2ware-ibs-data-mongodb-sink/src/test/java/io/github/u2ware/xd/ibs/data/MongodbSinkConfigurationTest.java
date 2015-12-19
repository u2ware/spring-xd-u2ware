package io.github.u2ware.xd.ibs.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
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
public class MongodbSinkConfigurationTest {

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

		
		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\", \"value\":\"a\", \"age\":18 }").build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\", \"value\":\"b\", \"age\":19 }").build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\", \"value\":\"c\", \"age\":19 }").build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\", \"value\":\"c\", \"age\":21 }").build());
		Thread.sleep(1000);

		
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoTemplate template = new MongoTemplate(mongoClient, "MyDatabase");
		List<DBObject> r = null;
		
		r= template.findAll(DBObject.class, "MyDatabase");
		Assert.assertEquals(1, r.size());
		logger.debug("\tMyDatabase");
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}

		
		r = template.findAll(DBObject.class, "Mina");
		Assert.assertEquals(3, r.size());
		logger.debug("\tMina");
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}
	}
}


