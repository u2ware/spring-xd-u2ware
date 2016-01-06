package io.github.u2ware.integration.netty.x;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpResourceServerTest {

    protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception{
				
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:9991/com/yourcompany/yourproject/index.html", String.class);
		logger.debug(result);
		Assert.assertEquals("<html><body><h1>com.yourcompany.yourproject</h1><h3>INDEX</h3></body></html>", result);
	}
}


