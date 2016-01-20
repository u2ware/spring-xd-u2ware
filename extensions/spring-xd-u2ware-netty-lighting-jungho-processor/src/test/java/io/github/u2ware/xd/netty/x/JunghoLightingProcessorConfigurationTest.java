package io.github.u2ware.xd.netty.x;

import io.github.u2ware.integration.netty.x.LightingJunghoRequest;

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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"use_json_input", "dont_use_splitter", "dont_use_json_output"})
public class JunghoLightingProcessorConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
	MessageChannel input;
    
    @Autowired @Qualifier("output")
	PollableChannel output;

	@Test
	public void test() throws Exception{

		for(int i=0 ; i < 10; i++){
			input.send(MessageBuilder.withPayload(new LightingJunghoRequest()).build());
			Message<?> message = output.receive();
			Assert.assertNotNull(message);
			logger.debug(message.getPayload());
			
			Thread.sleep(1000);
		}
	}
}


