package io.github.u2ware.xd.bacnet;

import io.github.u2ware.integration.bacnet.core.BacnetSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BacnetRestSourceConfigurationTest {

    private static BacnetSlave bacnetSlave;

	@BeforeClass
	public static void beforeClass() throws Exception{
		
		bacnetSlave = new BacnetSlave();
		bacnetSlave.setLocalPort(47808);
		bacnetSlave.setLocalInstanceNumber(47808);
		bacnetSlave.afterPropertiesSet();
	}    
	@AfterClass
	public static void afterClass() throws Exception{
		bacnetSlave.destroy();
	}	
	
    protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception{

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:9992", "{}", String.class);
		Assert.assertNotNull(result);
	}
}


