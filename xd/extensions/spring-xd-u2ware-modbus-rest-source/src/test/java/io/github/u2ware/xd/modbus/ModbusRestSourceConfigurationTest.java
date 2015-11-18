package io.github.u2ware.xd.modbus;

import io.github.u2ware.integration.modbus.core.ModbusSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"use_json_input", "dont_use_splitter", "use_json_output"})
public class ModbusRestSourceConfigurationTest {

	private static ModbusSlave modbusSlave;
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		modbusSlave = new ModbusSlave();
		modbusSlave.setLocalPort(10502);
		modbusSlave.afterPropertiesSet();
	}
	@AfterClass
	public static void afterClass() throws Exception{
		modbusSlave.destroy();
	}
	
    protected Log logger = LogFactory.getLog(getClass());


	@Test
	public void test() throws Exception{

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject("http://localhost:9992", "{\"unitId\":0,\"functionCode\":4,\"offset\":0,\"count\":6}", String.class);
		logger.debug(result);
		Assert.assertNotNull(result);

	}
}


