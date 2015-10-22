package io.github.u2ware.xd.mongodb;

import io.github.u2ware.xd.mongodb.test.Point;

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

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoDbExtSinkModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

	protected static MongodExecutable _mongodExe;
	protected static MongodProcess _mongod;
	protected static MongoClient mongoClient;
    
    private static SingleNodeApplication application;


	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		_mongodExe =  MongodStarter.getDefaultInstance()
				.prepare(new MongodConfigBuilder()
						.version(Version.Main.PRODUCTION)
						.net(new Net(27017, Network.localhostIsIPv6()))
						.build()
				);
		_mongod = _mongodExe.start();
		
		mongoClient = new MongoClient("localhost", 27017);
		
		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.sink, "mongodb-ext-sink"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		_mongod.stop();
		_mongodExe.stop();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "mongodb-ext-sink "
				+ " --databaseName=MyDatabase "
				+ " --host=127.0.0.1 "
				+ " --port=27017 "
				+ " --host=127.0.0.1 "
				+ " --idExpression=payload.id "
				+ " --valueExpression=payload.value ";

		logger.debug(processingChainUnderTest);
		
		//SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Thread.sleep(3000);


		chain.sendPayload(new Point("Mina", 19, "aaa"));
		Thread.sleep(1000);

		chain.sendPayload(new Point("Mina", 22, "bbb"));
		Thread.sleep(1000);
		
		chain.sendPayload(new Point("Mina", 19, "ccc"));
		Thread.sleep(1000);

		chain.sendPayload(new Point("Mina", 19, "ddd"));
		Thread.sleep(1000);
		
		MongoTemplate template = new MongoTemplate(mongoClient, "MyDatabase");
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



