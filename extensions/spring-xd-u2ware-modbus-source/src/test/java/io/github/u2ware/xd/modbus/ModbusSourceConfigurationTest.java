package io.github.u2ware.xd.modbus;

import io.github.u2ware.integration.modbus.core.ModbusResponse;
import io.github.u2ware.integration.modbus.core.ModbusSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"use_splitter", "dont_use_json_output"})
public class ModbusSourceConfigurationTest {

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

    @Autowired
	PollableChannel output;

	@Test
	public void test() {
		Message<?> message = output.receive(1000);
		logger.debug(message.getPayload());
		Assert.assertEquals(ModbusResponse.class, message.getPayload().getClass());
	}
}


