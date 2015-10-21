package io.github.u2ware.xd.netty;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HyundaiElevatorRestSourceConfigurationTest {

	private static HyundaiElevatorSlave elevatorSlave;
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		elevatorSlave = new HyundaiElevatorSlave();
		elevatorSlave.setPort(9991);
		elevatorSlave.afterPropertiesSet();
	}
	@AfterClass
	public static void afterClass() throws Exception{
		elevatorSlave.destroy();
	}
	
    protected Log logger = LogFactory.getLog(getClass());


	@Test
	public void test() throws Exception{
		Thread.sleep(3000);
		
		RestTemplate restTemplate = new RestTemplate();
		Object result = restTemplate.postForEntity("http://localhost:9992", "{}", Object.class);
		logger.debug(result.getClass());
		logger.debug(result);
	}
}


