package io.github.u2ware.xd.data;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongodbServer implements Runnable{

	protected static MongodExecutable _mongodExe;
	protected static MongodProcess _mongod;
	protected static int _mongodPort = 27017;

	@Override
	public void run() {
		try {
			MongodbServer.startup(_mongodPort);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception{
		
		try{
			_mongodPort = Integer.parseInt(args[0]);
		}catch(Exception e){
		}
		new Thread(new MongodbServer()).start();
	}
	
	public static void startup(int port) throws Exception{
		_mongodExe =  MongodStarter.getDefaultInstance()
				.prepare(new MongodConfigBuilder()
						.version(Version.Main.PRODUCTION)
						.net(new Net(port, Network.localhostIsIPv6()))
						.build()
				);
		_mongod = _mongodExe.start();
		
		MongoClient mongoClient = new MongoClient("localhost", port);
		MongoTemplate template = new MongoTemplate(mongoClient, "person");

		template.save(history(new DateTime(2016, 1, 3, 8, 3, 5), new Person("Mina",  56)), "Mina");
		template.save(history(new DateTime(2016, 1, 3, 10, 2, 5), new Person("Mina",   11)), "Mina");
		template.save(history(new DateTime(2016, 1, 3, 12, 1, 5), new Person("Mina", 23)), "Mina");		

		template.save(history(new DateTime(2016, 1, 11, 7, 3, 5), new Person("Mina",  40)), "Mina");
		template.save(history(new DateTime(2016, 1, 11, 11, 2, 5), new Person("Mina",   7)), "Mina");
		template.save(history(new DateTime(2016, 1, 11, 15, 1, 5), new Person("Mina", 13)), "Mina");		
		
		template.save(base(new DateTime(2016, 1, 7, 13, 1, 5), new Person("Mina", 13)), "person");
		template.save(base(new DateTime(2016, 1, 7, 13, 2, 5), new Person("Yok",  14)), "person");
		template.save(base(new DateTime(2016, 1, 7, 13, 3, 5), new Person("Joe",  15)), "person");
		

		Long min = new DateTime(2016, 1, 1, 7, 0).getMillis();
		Long max = new DateTime(2016, 1, 11, 11, 0).getMillis();
		
		AggregationOperation operation1 = TypedAggregation.match(Criteria.where("_id").gte(min).lte(max));
		System.err.println(operation1);
		AggregationOperation operation2 = TypedAggregation.group("payload").avg("value").as("avg");
		System.err.println(operation2);
		Aggregation aggregation = TypedAggregation.newAggregation(operation1, operation2);	
		AggregationResults<DBObject> result = template.aggregate(aggregation, "Mina", DBObject.class);
		System.err.println(result);
		System.err.println(result.getRawResults());
		System.err.println(result.getUniqueMappedResult());
		
		
		
		MongoTemplate minaTemplate = new MongoTemplate(mongoClient, "person");
		List<Entity> entities = minaTemplate.findAll(Entity.class, "Mina");
		System.err.println(entities);


		BasicDBObject q = new BasicDBObject();
		q.append("id", new BasicDBObject("$lt", max));
		Query query = new BasicQuery(q);
		Entity entity = minaTemplate.findOne(query, Entity.class, "Mina");
		System.err.println(entity);
	}
	
	private static Entity history(DateTime datetime, Person payload){
		
		Long timestamp = datetime.getMillis();//.currentTimeMillis();
		
		Entity objectToSave = new Entity();
		objectToSave.setId(timestamp);
		objectToSave.setValue(payload.getValue());
		objectToSave.setDatetime(new DateTime(timestamp).toString());
		objectToSave.setPayload(payload.getClass().getName());
		return objectToSave;
	}
	private static Entity base(DateTime datetime, Person payload){
		
		Long timestamp = datetime.getMillis();//.currentTimeMillis();
		
		Entity objectToSave = new Entity();
		objectToSave.setId(payload.getId());
		objectToSave.setValue(payload.getValue());
		objectToSave.setDatetime(new DateTime(timestamp).toString());
		objectToSave.setPayload(payload.getClass().getName());
		return objectToSave;
	}

	
	
	
	public static class Person{
		private String id;
		private Object value;
		
		public Person(){
		}

		public Person(String id, Object value) {
			super();
			this.id = id;
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Person [id=" + id + ", value=" + value + "]";
		}
		
	}
	
	public static void shutdown() throws Exception{
		_mongod.stop();
		_mongodExe.stop();
	}	
	
	

}
