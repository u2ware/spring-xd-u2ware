package io.github.u2ware.xd.bacnet;

import io.github.u2ware.integration.bacnet.core.BacnetRequest;
import io.github.u2ware.integration.bacnet.core.BacnetSlave;

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
@ActiveProfiles({"use_json_input", "dont_use_splitter", "use_json_output"})
public class BacnetProcessorConfigurationTest {

    private static BacnetSlave bacnetSlave;

	@BeforeClass
	public static void beforeClass() throws Exception{
		
		bacnetSlave = new BacnetSlave();
		bacnetSlave.setLocalPort(47809);
		bacnetSlave.setLocalInstanceNumber(47809);
		bacnetSlave.afterPropertiesSet();
	}    
	@AfterClass
	public static void afterClass() throws Exception{
		bacnetSlave.destroy();
	}	
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
	MessageChannel input;
    
    @Autowired @Qualifier("output")
	PollableChannel output;

	@Test
	public void test() throws Exception{

		input.send(MessageBuilder.withPayload(new BacnetRequest()).build());
		//input.send(MessageBuilder.withPayload("{}").build());
		//input.send(MessageBuilder.withPayload("aaaas").build());
		
		Message<?> message = output.receive(1000);
		logger.debug(message.getPayload());
		Assert.assertEquals(String.class, message.getPayload().getClass());
	}
}


