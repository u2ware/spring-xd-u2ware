package io.github.u2ware.integration.netty.x;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
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
public class JunghoLightingHostTest {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("lightingRequest")
	private MessageChannel lightingRequest;

    @Autowired @Qualifier("lightingResponse")
	private PollableChannel lightingResponse;

	@Test
	public void test() throws Exception{

		for(int i=0; i < 10; i++){

			Message<?> message = lightingResponse.receive();
			Assert.assertNotNull(message);
			logger.debug(message.getPayload());
			
			Thread.sleep(1000);
		}
	}
}


