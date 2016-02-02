package io.github.u2ware.xd.snmp;

import io.github.u2ware.integration.snmp.core.SnmpAgent;
import io.github.u2ware.integration.snmp.core.SnmpRequest;

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
public class SnmpProcessorConfigurationTest {


	@BeforeClass
	public static void beforeClass() throws Exception{
		SnmpAgent.startup(10161);
	}    
	@AfterClass
	public static void afterClass() throws Exception{
		SnmpAgent.shutdown(10161);
	}	
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
	MessageChannel input;
    
    @Autowired @Qualifier("output")
	PollableChannel output;

	@Test
	public void test() throws Exception{

		Object payload = new SnmpRequest("127.0.0.1", 10161, "1.3.6");
		input.send(MessageBuilder.withPayload(payload).build());
		//input.send(MessageBuilder.withPayload("{}").build());
		//input.send(MessageBuilder.withPayload("aaaas").build());
		
		Message<?> message = output.receive(1000);
		logger.debug(message.getPayload());
		Assert.assertEquals(String.class, message.getPayload().getClass());

	
		payload = "{\"host\":\"127.0.0.1\", \"port\":10161, \"rootOid\":\"1.3.6\"}";
		input.send(MessageBuilder.withPayload(payload).build());
		//input.send(MessageBuilder.withPayload("{}").build());
		//input.send(MessageBuilder.withPayload("aaaas").build());
		
		message = output.receive(1000);
		logger.debug(message.getPayload());
		Assert.assertEquals(String.class, message.getPayload().getClass());
	
	}
}


