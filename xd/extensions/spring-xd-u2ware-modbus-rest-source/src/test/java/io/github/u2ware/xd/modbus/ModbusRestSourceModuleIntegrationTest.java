package io.github.u2ware.xd.modbus;

import io.github.u2ware.integration.modbus.core.ModbusSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChain;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChainSupport;
import org.springframework.xd.module.ModuleType;

public class ModbusRestSourceModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

	private static ModbusSlave modbusSlave;
    
    private static SingleNodeApplication application;


	/**
	 * Start the single node container, binding random unused ports, etc. to not conflict with any other instances
	 * running on this host. Configure the ModuleRegistry to include the project module.
	 */
	@BeforeClass
	public static void beforeClass() throws Exception{
		modbusSlave = new ModbusSlave();
		modbusSlave.setLocalPort(10503);
		modbusSlave.afterPropertiesSet();

		//RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(
				new SingletonModuleRegistry(ModuleType.processor, "modbus-rest-source"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		modbusSlave.destroy();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "modbus-rest-source "
				+ " --httpPort=9994 "
				+ " --httpTimeout=10000 "
				+ " --host=127.0.0.1 "
				+ " --port=10503 ";

		logger.debug(processingChainUnderTest);
		
		//SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		

		Thread.sleep(3000);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:9994", "{\"unitId\":0,\"functionCode\":4,\"offset\":0,\"count\":6}", String.class);
		logger.debug(result);
		Assert.assertNotNull(result);


		chain.destroy();
	}
}



