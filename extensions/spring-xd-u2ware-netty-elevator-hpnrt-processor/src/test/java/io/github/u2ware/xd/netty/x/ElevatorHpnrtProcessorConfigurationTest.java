package io.github.u2ware.xd.netty.x;

import io.github.u2ware.integration.netty.x.ElevatorHpnrtRequest;
import io.github.u2ware.integration.netty.x.ElevatorHpnrtServerMock;

import java.util.Collection;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@ActiveProfiles({"use_json_input", "dont_use_splitter", "dont_use_json_output"})
public class ElevatorHpnrtProcessorConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		ElevatorHpnrtServerMock.startup(15002);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		ElevatorHpnrtServerMock.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
	MessageChannel input;

    
    @Autowired
	PollableChannel output;


	@Test
	public void test() throws Exception{

		for(int i=0 ; i < 6; i++){
			
			input.send(MessageBuilder.withPayload(new ElevatorHpnrtRequest()).build());
			
			Message<?> message = output.receive();
			Assert.assertNotNull(message);

			if(message.getPayload() instanceof Collection){
				Collection<?> payload = (Collection<?>)message.getPayload();
				logger.debug(payload.size());
			}else{
				logger.debug(0);
			}
			Thread.sleep(1000);
		}
	}
}


