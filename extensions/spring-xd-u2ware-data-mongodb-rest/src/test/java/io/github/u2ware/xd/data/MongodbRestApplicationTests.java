package io.github.u2ware.xd.data;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
		/*
		MvcResult mvcResult = this.mvc.perform(
					get("/current/person/Mina")
				).andDo(
						print()
				).andExpect(
					request().asyncStarted()
				).andReturn();

		logger.debug("---------------------------------");
		
		MvcResult mvcResult2 = this.mvc.perform(
				get("/current/person/Mina")
			).andDo(
					print()
			).andExpect(
				request().asyncStarted()
			).andReturn();
		
		logger.debug("---------------------------------");
		this.mvc.perform(
				asyncDispatch(mvcResult)
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
		logger.debug("---------------------------------");
		this.mvc.perform(
				asyncDispatch(mvcResult2)
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
		*/

		
		//////////////////////////////
		//
		//////////////////////////////
		this.mvc.perform(
				get("/current/person")
				.param("name", "k")
				.param("status", "interval")
			).andDo(
				print()
			).andExpect(
				status().isOk()
			);
		
		
		this.mvc.perform(
				get("/current/person/Mina")
			).andDo(
				print()
			).andExpect(
				status().isOk()
			);
		
		//////////////////////////////
		//
		//////////////////////////////
		this.mvc.perform(
				get("/record/person/Mina")
				.param("sort", "id,desc")
				//.param("payload", "criteria")

				//.param("min", "2016-01-03 00:00:00")
				//.param("max", "2016-01-05 00:00:00")
				
				//.param("datetime", "2016-01-11 00:00:00")
				//.param("interval", "HOUR")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		
		//////////////////////////////
		//
		//////////////////////////////
		this.mvc.perform(
				get("/recordChart/person/Mina")
//				.param("datetime", "2016-01-03 00:00:00")
//				.param("interval", "DAY")
//				.param("calculation", "AVG")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);

		//////////////////////////////
		//
		//////////////////////////////
		MvcResult r = this.mvc.perform(
				get("/setting/person")
		).andDo(
				print()
		).andExpect(
				status().isOk()
		).andReturn();
		
		MockMultipartFile file = new MockMultipartFile("file", "data.text", "text/html", r.getResponse().getContentAsByteArray());
		this.mvc.perform(
				fileUpload("/setting/person")
				.file(file)
		).andDo(
				print()
		).andExpect(
				status().isOk()
		);
    }    
}
