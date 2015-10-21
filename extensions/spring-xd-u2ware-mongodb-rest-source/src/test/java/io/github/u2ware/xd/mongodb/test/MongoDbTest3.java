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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MongoDbTest3 extends AbstractMongoDbTest{

	@Autowired
	private MongoDbFactory mongoDbFactory;
    
	@Autowired @Qualifier("mongoDbRequest")
	private QueueChannel mongodbRequest;
	
	@Autowired 
	private MongodbChannelGateway mongodbChannelGateway;

	public interface MongodbChannelGateway {
		public void send(DBObject object);
	}
	
	
	@Test
	public void testRunning() throws Exception {
		
		mongodbChannelGateway.send(new BasicDBObject("ccc", 34));

		Thread.sleep(2000);
		Assert.assertEquals(0, mongodbRequest.getQueueSize());
		
		
		MongoTemplate template = new MongoTemplate(mongoDbFactory);
		List<BasicDBObject> results = template.findAll(BasicDBObject.class, "copyPerson");
		Assert.assertEquals(1, results.size());
		
		
		BasicDBObject result = results.get(0);
		Assert.assertEquals(34, result.get("ccc"));
		
	}
}
