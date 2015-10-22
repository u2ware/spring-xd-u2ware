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
public class HttpServerSourceConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception {

		Thread.sleep(3000);
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:9992/a/b/c.html",  String.class);
		Assert.assertEquals("<html><body><h1>Hello World!!</h1></body></html>", result);
		
	}
}


