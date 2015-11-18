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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.mongodb.support.MongoHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MongoDbTest4 extends AbstractMongoDbTest{

	@Autowired
	private MongoDbFactory mongoDbFactory;
    
	@Autowired @Qualifier("mongoDbRequest")
	private MessageChannel mongodbRequest;
	
	public interface MongodbChannelGateway {
		public void send(DBObject object);
	}
	
	
	@Test
	public void testRunning() throws Exception {
		

		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		logger.debug(mongoTemplate);
		
		Message<?> message1 = MessageBuilder
				.withPayload(new Person("Mina", 34, "Mina"))
		        .setHeader(MongoHeaders.COLLECTION_NAME, "efg")
		        .build();
		mongodbRequest.send(message1);
		
		
		logger.debug(mongoTemplate.getCollectionNames());
		Object result = mongoTemplate.findAll(DBObject.class, "Minaxxxx");
		logger.debug(result);
		logger.debug(mongoTemplate.getCollectionNames());
		
		Assert.assertNotNull(result);
		
	}
}
