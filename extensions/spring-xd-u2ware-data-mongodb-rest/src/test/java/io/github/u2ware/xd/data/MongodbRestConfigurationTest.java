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

		List<Map> database = (List<Map>)restTemplate.getForObject("http://localhost:9898/raw/", List.class);
		//logger.debug("database:"+database);
		
		logger.debug("\t\t ");
		logger.debug("\t\t ");
		logger.debug("\t\t ");
		for(Map db : database){
			logger.debug(db);
			
			List<Map> collections = (List<Map>)restTemplate.getForObject("http://localhost:9898/raw/{database}", List.class, 
											db.get("databaseName"));
			//logger.debug("collections: "+collections);

			for(Map collection : collections){
				logger.debug("\t"+collection);

				List<Map> documents = restTemplate.getForObject("http://localhost:9898/raw/{database}/{collectionName}", List.class, 
										db.get("databaseName"), 
										collection.get("collectionName"));
				//logger.debug("documents: "+documents);
				
				for(Map document : documents){
					//logger.debug("document: "+document);

					String entity = restTemplate.getForObject("http://localhost:9898/raw/{database}/{collectionName}/{id}", String.class, 
							db.get("databaseName"), 
							collection.get("collectionName"),
							document.get("id"));
					if(entity != null){
						logger.debug("\t\t"+entity);
					}
				}
			}
		}
	}
}


