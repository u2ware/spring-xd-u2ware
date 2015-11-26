package io.github.u2ware.xd.rest;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class RestMongodbServerConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception {
		MongodbServer.startup(27017);
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		MongodbServer.shutdown();
	}
	
    protected Log logger = LogFactory.getLog(getClass());


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test() throws Exception{
		
		Thread.sleep(2000);
		RestTemplate restTemplate = new RestTemplate();
		
		logger.debug("");
		logger.debug("");
		Thread.sleep(2000);
		List<Map> database = restTemplate.getForObject("http://localhost:9999", List.class);
		for(Map db : database){
			logger.debug(db);
			
			List<Map> collections = restTemplate.getForObject("http://localhost:9999/{database}", List.class, 
																db.get("databaseName"));
			
			for(Map collection : collections){
				logger.debug("\t"+collection);
				List<Map> entities = restTemplate.getForObject("http://localhost:9999/{database}/{collectionName}", List.class, 
										db.get("databaseName"), 
										collection.get("collectionName"));
				
				for(Map e : entities){

					String entity = restTemplate.getForObject("http://localhost:9999/{database}/{collectionName}/{id}", String.class, 
							db.get("databaseName"), 
							collection.get("collectionName"),
							e.get("_id"));
					logger.debug("\t\t"+entity);
					
				}
			}
		}
		List result4 = restTemplate.postForObject("http://localhost:9999/personDb/person","{'name':'Joe'}", List.class);
		logger.debug(result4);
		Assert.assertEquals(1, result4.size());
	}
}


