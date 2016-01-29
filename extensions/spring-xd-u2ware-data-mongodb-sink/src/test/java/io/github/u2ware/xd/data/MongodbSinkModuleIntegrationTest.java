package io.github.u2ware.xd.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainProducer;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongodbSinkModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

    private static SingleNodeApplication application;


	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		MongodbServer.startup(27017);

		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.sink, "data-mongodb-sink"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		MongodbServer.shutdown();
	}
	

	@Test
	public void test() throws Exception {

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoTemplate template = new MongoTemplate(mongoClient, "MyDatabase");
		template.createCollection("Mina");
		
		
		String streamName = "streamTest";

		String processingChainUnderTest = "data-mongodb-sink "
				+ " --databaseName=MyDatabase "
				+ " --host=127.0.0.1 "
				+ " --port=27017 "
				+ " --idExpression=payload.id "
				+ " --valueExpression=payload.value ";

		logger.debug(processingChainUnderTest);
		
		//SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Thread.sleep(3000);

		chain.sendPayload("{\"id\":\"Mina\", \"value\":\"a\", \"age\":18 }");
		Thread.sleep(1000);

		chain.sendPayload("{\"id\":\"Mina\", \"value\":\"b\", \"age\":19 }");
		Thread.sleep(1000);
		
		chain.sendPayload("{\"id\":\"Mina\", \"value\":\"c\", \"age\":20 }");
		Thread.sleep(1000);

		chain.sendPayload("{\"id\":\"Mina\", \"value\":\"c\", \"age\":21 }");
		Thread.sleep(1000);
		
		
		List<DBObject> r = null;
		
		r= template.findAll(DBObject.class, "MyDatabase");
		Assert.assertEquals(1, r.size());
		logger.debug("\tMyDatabase");
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}

		r = template.findAll(DBObject.class, "Mina");
		logger.debug("\tMina");
		Assert.assertEquals(3, r.size());
		for(DBObject e : r){
			logger.debug("\t\t"+e);
		}

		chain.destroy();
	}
}



