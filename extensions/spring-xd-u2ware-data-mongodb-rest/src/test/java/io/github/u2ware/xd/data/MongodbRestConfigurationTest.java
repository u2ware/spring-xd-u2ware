package io.github.u2ware.xd.data;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MongodbRestConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws Exception {
		MongodbServer.startup(27018);
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

		Map database = restTemplate.getForObject("http://localhost:9898/raw/", Map.class);
		//logger.debug("database:"+database);
		List<Map> databaseContent = (List<Map>)database.get("content");
		
		logger.debug("\t\t ");
		logger.debug("\t\t ");
		logger.debug("\t\t ");
		for(Map db : databaseContent){
			logger.debug(db);
			
			Map collections = restTemplate.getForObject("http://localhost:9898/raw/{database}", Map.class, 
											db.get("databaseName"));
			//logger.debug("collections: "+collections);
			List<Map> collectionsContent = (List<Map>)collections.get("content");
			for(Map collection : collectionsContent){
				logger.debug("\t"+collection);
			
				Map entities = restTemplate.getForObject("http://localhost:9898/raw/{database}/{collectionName}?size=17", Map.class, 
										collection.get("databaseName"), 
										collection.get("collectionName"));
				//logger.debug("entities: "+entities);
				List<Map> entitiesContent = (List<Map>)entities.get("content");
				for(Map e : entitiesContent){
					
					//logger.debug("entity: "+e);
					String entity = restTemplate.getForObject("http://localhost:9898/raw/{database}/{collectionName}/{id}", String.class, 
							collection.get("databaseName"), 
							collection.get("collectionName"),
							e.get("id"));

					logger.debug("\t\t"+entity);
				}
			}
		}
	}
}


