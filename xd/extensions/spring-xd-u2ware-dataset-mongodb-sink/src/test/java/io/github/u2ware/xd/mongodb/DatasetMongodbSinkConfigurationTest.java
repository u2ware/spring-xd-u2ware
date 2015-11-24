package io.github.u2ware.xd.mongodb;

import io.github.u2ware.xd.mongodb.test.Point;

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

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles("use_json_input")
public class DatasetMongodbSinkConfigurationTest {

	protected static MongodExecutable _mongodExe;
	protected static MongodProcess _mongod;
	protected static MongoClient mongoClient;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		_mongodExe =  MongodStarter.getDefaultInstance()
				.prepare(new MongodConfigBuilder()
						.version(Version.Main.PRODUCTION)
						.net(new Net(27017, Network.localhostIsIPv6()))
						.build()
				);
		_mongod = _mongodExe.start();
		
		mongoClient = new MongoClient("localhost", 27017);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		_mongod.stop();
		_mongodExe.stop();
	}
	
    protected Log logger = LogFactory.getLog(getClass());


    @Autowired @Qualifier("input")
    MessageChannel input;
    
	@Test
	public void test() throws Exception{


		input.send(MessageBuilder.withPayload(new Point("Mina", 19, "aaa")).build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload(new Point("Mina", 22, "bbb")).build());
		Thread.sleep(1000);
		
		input.send(MessageBuilder.withPayload(new Point("Mina", 19, "ccc")).build());
		Thread.sleep(1000);

		input.send(MessageBuilder.withPayload(new Point("Mina", 19, "ddd")).build());
		Thread.sleep(1000);
		
		/*
		input.send(MessageBuilder.withPayload("{\"id\":\"Mina\"}").build());
		Thread.sleep(1000);

		Tuple tuple6 = TupleBuilder.tuple().of("id", "aaa", "value", "bbb");
		input.send(MessageBuilder.withPayload(tuple6).build());
		logger.debug(tuple6);
		logger.debug(MessageBuilder.withPayload(tuple6).build());
		Thread.sleep(1000);
		*/
		
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
	}
}


