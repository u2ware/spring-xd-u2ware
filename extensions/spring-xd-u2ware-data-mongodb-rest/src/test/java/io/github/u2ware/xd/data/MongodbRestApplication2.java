package io.github.u2ware.xd.data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MongodbRestApplication.class)
@WebAppConfiguration
public class MongodbRestApplication2 {

    protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

    @Test
	public void contextLoads() throws Exception {

		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for(String beanName : beanNames){
			Object beanObject = context.getBean(beanName);
			if(beanObject != null){
				logger.debug(beanName+"="+beanObject.getClass());
			}else{
				logger.debug(beanName+"="+null);
			}
		}
		
		this.mvc.perform(
				get("/chart/bacnet/47808_2_0")
				.param("datetime", "2016-01-11 22:58:00")
				.param("interval", "REALTIME")
				.param("calculation", "AVG")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
    }    

}
