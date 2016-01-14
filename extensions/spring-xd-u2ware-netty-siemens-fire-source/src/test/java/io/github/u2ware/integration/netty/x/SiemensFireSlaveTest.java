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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SiemensFireSlaveTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		SiemensFireMaster.startup("127.0.0.1",10901);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		SiemensFireMaster.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    
    @Autowired @Qualifier("fireRequest")
	private MessageChannel fireRequest;
    
    @Autowired @Qualifier("fireResponse")
	private PollableChannel fireResponse;

	@Test
	public void test() throws Exception{

		Message<?> message = fireResponse.receive();
		logger.debug(message.getPayload());

		Assert.assertEquals(SiemensFireResponse.class, message.getPayload().getClass());
	}
}


