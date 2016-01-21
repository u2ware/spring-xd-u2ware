package io.github.u2ware.xd.data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import io.github.u2ware.xd.data.MongodbRestApplication;

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


		//////////////////////////////
		//
		//////////////////////////////
		this.mvc.perform(
				get("/monitor/person/Mina")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		this.mvc.perform(
				get("/alarm/person/Mina")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
		
		
		this.mvc.perform(
				get("/history/person/Mina")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
		
		this.mvc.perform(
				get("/chart/person/Mina")
				.param("datetime", "2015-02-11 00:00:00")
				.param("interval", "DAY")
				.param("calculation", "AVG")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
		
		
		/*
		this.mvc.perform(
				get("/alarm/person/Mina")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		MvcResult mvcResult = this.mockMvc.perform(get("/async/callable/response-body"))
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult("Callable result"))
				.andReturn();

			this.mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(content().string("Callable result"));
		*/
    
    }    

}
