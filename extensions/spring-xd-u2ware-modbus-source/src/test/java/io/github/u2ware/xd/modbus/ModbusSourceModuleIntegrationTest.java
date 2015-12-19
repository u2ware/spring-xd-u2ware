package io.github.u2ware.xd.modbus;

import io.github.u2ware.integration.modbus.core.ModbusResponse;
import io.github.u2ware.integration.modbus.core.ModbusSlave;

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

public class ModbusSourceModuleIntegrationTest {
	
    protected Log logger = LogFactory.getLog(getClass());

	private static ModbusSlave modbusSlave;
    
    private static SingleNodeApplication application;

	private static int RECEIVE_TIMEOUT = 6000;

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
				new SingletonModuleRegistry(ModuleType.source, "modbus-source"));
	}

	
	@AfterClass
	public static void afterClass() throws Exception{
		modbusSlave.destroy();
	}
	

	@Test
	public void test() throws InterruptedException {

		String streamName = "streamTest";

		String processingChainUnderTest = "modbus-source "

				+ " --host=127.0.0.1 "
				+ " --port=10503 "

				+ " --unitId=0 "
				+ " --functionCode=4 "
				+ " --offset=0 "
				+ " --count=6 "
				
				+ " --fixedDelay=5000"
				+ " --jsonOutput=false ";

		logger.debug(processingChainUnderTest);
		
		SingleNodeProcessingChainConsumer chain = SingleNodeProcessingChainSupport.chainConsumer(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChain chain = SingleNodeProcessingChainSupport.chain(application, streamName, processingChainUnderTest);
		//SingleNodeProcessingChainProducer chain = SingleNodeProcessingChainSupport.chainProducer(application, streamName, processingChainUnderTest);
		
		Object payload = chain.receivePayload(RECEIVE_TIMEOUT);
		Assert.assertEquals(ModbusResponse.class, payload.getClass());

		chain.destroy();
	}
}



