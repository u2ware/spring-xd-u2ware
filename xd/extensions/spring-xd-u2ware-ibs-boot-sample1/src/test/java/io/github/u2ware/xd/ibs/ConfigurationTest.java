package io.github.u2ware.xd.ibs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
   
    @BeforeClass
	public static void beforeClass() throws Exception {
		MongodbServer.startup(27017);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		MongodbServer.shutdown();
	}

	@Test
	public void test() throws Exception {

		Thread.sleep(3000);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:10801/helloworld",  String.class);
		logger.debug(result);
		Assert.assertEquals("hello world", result.getBody());
		
		String result2 = restTemplate.getForObject("http://localhost:10801/webjars/angularjs/1.4.8/angular.min.js",  String.class);
		logger.debug(result2);
		Assert.assertNotNull(result2);
		
		String result3 = restTemplate.getForObject("http://localhost:10801/person",  String.class);
		logger.debug(result3);
		Assert.assertNotNull(result3);
	}
}


