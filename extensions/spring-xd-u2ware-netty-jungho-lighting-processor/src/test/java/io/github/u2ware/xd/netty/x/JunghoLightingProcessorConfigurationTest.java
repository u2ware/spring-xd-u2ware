package io.github.u2ware.xd.netty.x;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
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
@ActiveProfiles({"use_json_input", "dont_use_splitter", "use_json_output"})
public class JunghoLightingProcessorConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
	PollableChannel output;

	@Test
	public void test() throws Exception{

		for(int i=0; i<10; i++){
			Message<?> message = output.receive(100000);
			Assert.assertNotNull(message);
			logger.debug(message.getPayload());
			
			Thread.sleep(1000);
		}
	}
}


