package io.github.u2ware.xd.netty;

import io.github.u2ware.xd.netty.SiemensFireviewMaster;

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
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SiemensFireviewChannelAdapterTest {

	private static SiemensFireviewMaster fireMaster;
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		fireMaster = new SiemensFireviewMaster();
		fireMaster.setHost("localhost");
		fireMaster.setPort(10810);
		fireMaster.afterPropertiesSet();
	}
	@AfterClass
	public static void afterClass() throws Exception{
		fireMaster.destroy();
	}
	
    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("fireResponse")
	private PollableChannel fireResponse;

	@Test
	public void test() throws Exception{

		logger.debug(fireResponse);
		Message<?> message = fireResponse.receive(10000);
		Assert.assertNotNull(message);

		logger.debug(message);
	}
}


