package io.github.u2ware.xd.snmp;

import io.github.u2ware.integration.snmp.core.SnmpAgent;
import io.github.u2ware.integration.snmp.core.SnmpResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
@ActiveProfiles({"use_splitter", "dont_use_json_output"})
public class SnmpSourceConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		SnmpAgent.startup(10162);
		SnmpAgent.startup(10163);
	}    
	@AfterClass
	public static void afterClass() throws Exception{
		SnmpAgent.shutdown(10162);
		SnmpAgent.shutdown(10163);
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
	private PollableChannel output;

	@Test
	public void test() throws Exception{

		Message<?> message = output.receive();
		logger.debug(message.getPayload());
		Assert.assertEquals(SnmpResponse.class, message.getPayload().getClass());
		
		
		Thread.sleep(10000);
	}
}


