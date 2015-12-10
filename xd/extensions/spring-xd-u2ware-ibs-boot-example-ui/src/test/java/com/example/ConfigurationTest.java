package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception {

		Thread.sleep(3000);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:10801/helloworld",  String.class);
		logger.debug(result);
		Assert.assertEquals("hello world", result.getBody());
		
	}
}


