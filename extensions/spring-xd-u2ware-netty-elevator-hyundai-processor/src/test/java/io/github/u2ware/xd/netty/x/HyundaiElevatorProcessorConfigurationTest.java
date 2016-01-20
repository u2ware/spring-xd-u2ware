package io.github.u2ware.xd.netty.x;

import io.github.u2ware.integration.netty.x.ElevatorHyundaiServerMock;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"use_json_input", "dont_use_splitter", "use_json_output"})
public class HyundaiElevatorProcessorConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		ElevatorHyundaiServerMock.startup(9991);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		ElevatorHyundaiServerMock.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
	MessageChannel input;

    
    @Autowired
	PollableChannel output;


	@Test
	public void test() throws Exception{

		Thread.sleep(3000);
		Message<?> message = output.receive();
		logger.debug(message.getPayload());
		Assert.assertEquals(String.class, message.getPayload().getClass());
	}
}


