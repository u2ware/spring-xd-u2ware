package io.github.u2ware.xd.netty;

import io.github.u2ware.xd.netty.HyundaiElevatorSlave;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HyundaiElevatorChannelAdapterTest {

	private static HyundaiElevatorSlave elevatorSlave;
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		elevatorSlave = new HyundaiElevatorSlave();
		elevatorSlave.setPort(10911);
		elevatorSlave.afterPropertiesSet();
	}
	@AfterClass
	public static void afterClass() throws Exception{
		elevatorSlave.destroy();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("elevatorResponse")
	private PollableChannel elevatorResponse;

	@Test
	public void test() throws Exception{

		logger.debug(elevatorResponse);
		Message<?> message = elevatorResponse.receive(10000);
		Assert.assertNotNull(message);

		logger.debug(message.getClass());
		logger.debug(message.getPayload().getClass());
	}
}


