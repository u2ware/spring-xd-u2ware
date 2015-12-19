package io.github.u2ware.xd.ibs;

import io.github.u2ware.xd.ibs.controller.CurrentData;

import org.springframework.data.mongodb.core.MongoTemplate;

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

		template.save(create("x"), "Mina");
		template.save(create("y"), "Mina");
		template.save(create("a"), "Mina");

		template.save(create("Mina",  "a"), "person");
		template.save(create("Joe",  "b"), "person");
		template.save(create("Yok",  "c"), "person");
	}
	
	private static CurrentData create(String id, Object value){
		CurrentData objectToSave = new CurrentData();
		objectToSave.setId(id);
		objectToSave.setValue(value);
		objectToSave.setTimestamp(System.currentTimeMillis());
		return objectToSave;
	}
	private static CurrentData create(Object value){
		CurrentData objectToSave = new CurrentData();
		Long id = System.currentTimeMillis();
		objectToSave.setId(""+id);
		objectToSave.setValue(value);
		objectToSave.setTimestamp(id);
		return objectToSave;
	}
	
	
	public static void shutdown() throws Exception{
		_mongod.stop();
		_mongodExe.stop();
	}	
	
	

}
