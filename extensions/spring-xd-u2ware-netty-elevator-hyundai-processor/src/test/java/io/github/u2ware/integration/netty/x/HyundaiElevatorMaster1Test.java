package io.github.u2ware.integration.netty.x;

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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HyundaiElevatorMaster1Test {
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		HyundaiElevatorSlave.startup(9898);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		HyundaiElevatorSlave.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("elevatorRequest")
	private MessageChannel elevatorRequest;

    @Autowired @Qualifier("elevatorResponse")
	private PollableChannel elevatorResponse;

	@Test
	public void test() throws Exception{

		Thread.sleep(3000);
		elevatorRequest.send(MessageBuilder.withPayload(new HyundaiElevatorRequest()).build());

		Message<?> message = elevatorResponse.receive(10000);
		Assert.assertNotNull(message);

		logger.debug(message.getClass());
		logger.debug(message.getPayload());
	}
}


