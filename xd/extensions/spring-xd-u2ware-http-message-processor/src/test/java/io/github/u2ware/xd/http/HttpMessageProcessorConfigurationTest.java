package io.github.u2ware.xd.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpMessageProcessorConfigurationTest {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired @Qualifier("input")
    private MessageChannel input;
    
	@Test
	public void test() throws Exception {

		Thread.sleep(3000);
		
		input.send(MessageBuilder.withPayload("hello world").build());
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:9992",  String.class);
		Assert.assertEquals("hello world", result);
		
	}
}


