package io.github.u2ware.xd.mongodb;

import io.github.u2ware.xd.mongodb.test.Person;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

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
public class MongoDbRestSourceConfigurationTest {

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


	@Test
	public void test() throws Exception{

		MongoTemplate template = new MongoTemplate(mongoClient, "mydatabase");
		template.save(new Person("Mina", 12, "a"));
		template.save(new Person("Joe", 36, "b"));
		template.save(new Person("Yok", 27, "c"));
		
		
		Thread.sleep(2000);
		RestTemplate restTemplate = new RestTemplate();
		
		
		restTemplate.put("http://localhost:10501/yourdatabase/abcd", null);
		

		String result1 = restTemplate.postForObject("http://localhost:10501", null, String.class);
		Assert.assertNotNull(result1);
		logger.debug(result1);
		logger.debug("");
		
		String result2 = restTemplate.postForObject("http://localhost:10501/mydatabase/", null, String.class);
		Assert.assertNotNull(result2);
		logger.debug(result2);
		logger.debug("");


		String result3 = restTemplate.postForObject("http://localhost:10501/mydatabase/person", null, String.class);
		Assert.assertNotNull(result3);
		logger.debug(result3);
		logger.debug("");
		
		String result4 = restTemplate.postForObject("http://localhost:10501/mydatabase/person","{'name':'Joe'}", String.class);
		Assert.assertNotNull(result4);
		logger.debug(result4);
		logger.debug("");
		
	}
}


