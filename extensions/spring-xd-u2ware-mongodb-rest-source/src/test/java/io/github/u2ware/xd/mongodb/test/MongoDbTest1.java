/* Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.github.u2ware.xd.mongodb.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MongoDbTest1 extends AbstractMongoDbTest{

	@Autowired
	private Mongo mongoDb;
	
	@Autowired
	private MongoDbFactory mongoDbFactory;
	
	@Test
	public void testRunning() throws Exception {
		
		logger.debug(mongoDb);
		logger.debug(mongoDbFactory);
		for(String databaseName : mongoDb.getDatabaseNames()){
			logger.debug("\t"+databaseName);
		}
		
		logger.debug(mongoDbFactory.getDb().getName());
		logger.debug(mongoDbFactory.getDb().getCollectionNames());
		logger.debug(mongoDbFactory.getDb("sample1").getName());
		logger.debug(mongoDbFactory.getDb("sample1").getCollectionNames());

		
		if(mongoDb.getDB("u2ware").collectionExists("my")){
			mongoDb.getDB("u2ware").getCollection("my").drop();
			logger.debug("remove");
		}else{
			DBCollection b = mongoDb.getDB("u2ware").createCollection("my", new BasicDBObject());
			logger.debug("createCollection "+b);
		}
		
		
		
	    
		//MongoDbFactory f = new SimpleMongoDbFactory(mongo, databaseName);
				
		//MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		/*
		mongoOps.insert(new Person("Joe", 34));
	    mongoOps.save(new Person("Joe", 36));
	    logger.info(mongoOps.findOne(new Query(where("name").is("Joe")), Person.class));
	    //mongoOps.dropCollection("person");
	    logger.info(mongoOps.findAll(Person.class));
	    */
	    
	    /*
	    BasicDBObject d = new BasicDBObject();
	    d.put("_id", "cc");
	    d.put("bbb", "bb");
		mongoOps.insert(d, "yourColl");
		*/

	}
}
