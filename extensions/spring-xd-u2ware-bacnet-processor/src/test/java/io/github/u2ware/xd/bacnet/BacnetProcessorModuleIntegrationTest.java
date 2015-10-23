package io.github.u2ware.xd.bacnet;

import io.github.u2ware.integration.bacnet.core.BacnetSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChain;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

public class BacnetProcessorModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

    private static BacnetSlave bacnetSlave;
    
    private static SingleNodeApplication application;

	private static int RECEIVE_TIMEOUT = 6000;

	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		bacnetSlave = new BacnetSlave();
		bacnetSlave.setLocalPort(47909);
		bacnetSlave.setLocalInstanceNumber(47909);
		bacnetSlave.afterPropertiesSet();
		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.processor, "bacnet-processor"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		bacnetSlave.destroy();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "bacnet-processor "
				+ " --remoteAddress=127.0.0.1:47909 "
				+ " --remoteInstanceNumber=47909 ";

		logger.debug(processingChainUnderTest);
		
		//SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		

		chain.sendPayload("{}");
		
		Object payload = chain.receivePayload(RECEIVE_TIMEOUT);
		Assert.assertEquals(String.class, payload.getClass());

		chain.destroy();
	}
}



