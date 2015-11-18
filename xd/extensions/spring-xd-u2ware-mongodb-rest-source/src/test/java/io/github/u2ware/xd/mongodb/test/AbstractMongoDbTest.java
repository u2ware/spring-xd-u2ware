package io.github.u2ware.xd.mongodb.test;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class AbstractMongoDbTest {

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

    protected @Autowired ApplicationContext applicationContext;

	@Before
	public void before() throws Exception {
		logger.warn("===================================================");
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames, 0, beanNames.length);
        for(String name : beanNames){
            logger.warn(name+"="+applicationContext.getBean(name).getClass());
        }
        logger.warn("===================================================");
	}
	
	
}
