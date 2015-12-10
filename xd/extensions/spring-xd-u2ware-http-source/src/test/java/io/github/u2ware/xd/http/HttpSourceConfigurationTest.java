package io.github.u2ware.xd.http;

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
public class HttpSourceConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception {

		Thread.sleep(3000);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:9992/com/yourcompany/yourproject/index.html",  String.class);
		logger.debug(result);
		Assert.assertNotNull(result);
		
		
	}
}


