package com.mycompany.myproject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainConsumer;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

public class HelloWorldModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

    private static SingleNodeApplication application;

	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.source, "webmvc-helloworld"));
	}

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "webmvc-helloworld "
				+ " --httpPort=9993 ";

		logger.debug(processingChainUnderTest);
		
		SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		

		Thread.sleep(3000);

		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:9993/hello/world",  String.class);
		logger.debug(result);
		Assert.assertEquals("hello world", result);
		
		
		chain.destroy();
	}
}



