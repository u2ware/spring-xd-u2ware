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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MongoDbTest2 extends AbstractMongoDbTest{

	@Autowired
	private MongoDbFactory mongoDbFactory;
    	
	@Autowired @Qualifier("mongoDbResponse")
	private QueueChannel mongoDbResponse;

	@SuppressWarnings("unchecked")
	@Test
	public void testRunning() throws Exception {
		
		MongoOperations mongoOps = new MongoTemplate(mongoDbFactory);
		mongoOps.save(new Person("Joe", 34));
	    mongoOps.save(new Person("Joe", 36));
	    mongoOps.save(new Person("Foo", 21));
	    mongoOps.save(new Person("Bar", 28));
		
		Thread.sleep(3000);
		
		Message<?> message = mongoDbResponse.receive();
		List<Person> payload = (List<Person>)message.getPayload();
		Assert.assertEquals(2, payload.size());

	}
}
