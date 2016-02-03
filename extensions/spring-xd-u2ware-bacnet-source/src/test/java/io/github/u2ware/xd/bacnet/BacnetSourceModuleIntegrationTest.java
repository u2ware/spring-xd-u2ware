package io.github.u2ware.xd.bacnet;

import io.github.u2ware.integration.bacnet.core.BacnetResponse;
import io.github.u2ware.integration.bacnet.core.BacnetDevice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainConsumer;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

public class BacnetSourceModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

    private static SingleNodeApplication application;

	private static int RECEIVE_TIMEOUT = 6000;

	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		BacnetDevice.startup(47909);
		
		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.source, "bacnet-source"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		application.close();
		BacnetDevice.shutdown(47909);
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "bacnet-source "
				+ " --localPort=47808"
				+ " --requestSupport=127.0.0.1:47909:47909 "
				+ " --fixedDelay=5000"
				+ " --jsonOutput=false ";

		logger.debug(processingChainUnderTest);
		
		SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Object payload = chain.receivePayload(RECEIVE_TIMEOUT);
		Assert.assertEquals(BacnetResponse.class, payload.getClass());

		chain.destroy();
	}
}



