package io.github.u2ware.xd.bacnet;

import io.github.u2ware.integration.bacnet.core.BacnetResponse;
import io.github.u2ware.integration.bacnet.core.BacnetSlave;

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
public class BacnetSourceConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		BacnetSlave.startup(47908);
	}    
	@AfterClass
	public static void afterClass() throws Exception{
		BacnetSlave.shutdown();
	}	
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
	private PollableChannel output;

	@Test
	public void test() {

		Message<?> message = output.receive(1000);
		logger.debug(message.getPayload());
		Assert.assertEquals(BacnetResponse.class, message.getPayload().getClass());
	}
}


