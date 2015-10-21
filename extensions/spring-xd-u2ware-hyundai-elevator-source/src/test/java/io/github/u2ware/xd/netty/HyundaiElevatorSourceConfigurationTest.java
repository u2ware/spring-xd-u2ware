package io.github.u2ware.xd.netty;

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
public class HyundaiElevatorSourceConfigurationTest {

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

    @Autowired
	PollableChannel output;

	@Test
	public void test() {

		Message<?> message = output.receive(10000);
		logger.debug(message.getPayload());
		Assert.assertEquals(HyundaiElevatorResponse.class, message.getPayload().getClass());
		
	}
}


