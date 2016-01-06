package io.github.u2ware.xd.netty.x;

import io.github.u2ware.integration.netty.x.SiemensFireMaster;
import io.github.u2ware.integration.netty.x.SiemensFireResponse;

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
@ActiveProfiles({"dont_use_json_output"})
public class SiemensFireSourceConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception{
		SiemensFireMaster.startup("127.0.0.1", 10902);
	}
	@AfterClass
	public static void afterClass() throws Exception{
		SiemensFireMaster.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
	PollableChannel output;

	@Test
	public void test() {

		Message<?> message = output.receive(10000);
		logger.debug(message.getPayload());

		Assert.assertEquals(SiemensFireResponse.class, message.getPayload().getClass());
	}
}


