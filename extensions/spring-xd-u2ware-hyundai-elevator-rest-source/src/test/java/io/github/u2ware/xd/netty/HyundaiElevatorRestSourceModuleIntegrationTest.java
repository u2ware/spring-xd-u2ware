package io.github.u2ware.xd.netty;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainConsumer;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

public class HyundaiElevatorRestSourceModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

	private static HyundaiElevatorSlave elevatorSlave;
    
    private static SingleNodeApplication application;


	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		elevatorSlave = new HyundaiElevatorSlave();
		elevatorSlave.setPort(9993);
		elevatorSlave.afterPropertiesSet();
		
		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.source, "hyundai-elevator-rest-source"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		elevatorSlave.destroy();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "hyundai-elevator-rest-source "
				+ " --httpPort=9994 "
				+ " --httpTimeout=10000 "
				+ " --host=127.0.0.1 "
				+ " --port=9993 ";

		logger.debug(processingChainUnderTest);
		
		SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Thread.sleep(6000);
		RestTemplate restTemplate = new RestTemplate();
		Object result = restTemplate.postForEntity("http://localhost:9994", "{}", Object.class);
		logger.debug(result);

		chain.destroy();
	}
}



