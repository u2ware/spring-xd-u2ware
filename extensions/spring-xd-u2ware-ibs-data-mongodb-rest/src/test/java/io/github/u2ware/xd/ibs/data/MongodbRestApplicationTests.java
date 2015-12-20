package io.github.u2ware.xd.ibs.data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import io.github.u2ware.xd.ibs.data.MongodbRestApplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class MongodbRestApplicationTests {

    protected Log logger = LogFactory.getLog(getClass());

    @BeforeClass
	public static void beforeClass() throws Exception {
		MongodbServer.startup(27017);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		MongodbServer.shutdown();
	}

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
						get("/")
				).andDo(
						print()
				).andExpect(
						status().isOk()
				);

		this.mvc.perform(
				get("/person")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		this.mvc.perform(
				get("/person/person")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		this.mvc.perform(
				get("/person/person/Mina")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
    }    

}
