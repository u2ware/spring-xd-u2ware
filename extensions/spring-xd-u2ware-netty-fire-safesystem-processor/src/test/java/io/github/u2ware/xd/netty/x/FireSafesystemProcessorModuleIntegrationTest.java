package io.github.u2ware.xd.netty.x;

import io.github.u2ware.integration.netty.x.FireSafesystemServerMock;

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

public class FireSafesystemProcessorModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

    
    private static SingleNodeApplication application;

	private static int RECEIVE_TIMEOUT = 12000;

	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		FireSafesystemServerMock.startup(12003);
		
		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.processor, "fire-safesystem-processor"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		application.close();
		FireSafesystemServerMock.shutdown();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "fire-safesystem-processor "
				+ " --host=127.0.0.1 "
				+ " --port=12003 ";

		logger.debug(processingChainUnderTest);
		
		//SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Object payload = chain.receivePayload(RECEIVE_TIMEOUT);
		logger.debug(payload);
		Assert.assertNotNull(payload);

		chain.destroy();
	}
}



