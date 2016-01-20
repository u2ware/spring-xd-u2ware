package io.github.u2ware.integration.netty.x;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ElevatorHpnrtClientTest {
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		ElevatorHpnrtServerMock.startup(15001);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		ElevatorHpnrtServerMock.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("elevatorRequest")
	private MessageChannel elevatorRequest;

    @Autowired @Qualifier("elevatorResponse")
	private PollableChannel elevatorResponse;

	@Test
	public void test() throws Exception{

		Thread.sleep(10000);
	}
}


