package io.github.u2ware.xd.rest;

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
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoTemplate template = new MongoTemplate(mongoClient, "personDb");
		template.save(new Person("Mina", 12, "a"));
		template.save(new Person("Joe", 36, "b"));
		template.save(new Person("Yok", 27, "c"));
	}
	
	public static void shutdown() throws Exception{
		_mongod.stop();
		_mongodExe.stop();
	}	
	
	
	public static class Person {
		  private String id;
		  private String name;
		  private int age;

		  public Person() {
			  
		  }
		  public Person(String name, int age) {
		    this.name = name;
		    this.age = age;
		  }
		  public Person(String name, int age, String id) {
			    this.id = id;
			    this.name = name;
			    this.age = age;
		  }

		  public String getId() {
		    return id;
		  }
		  public String getName() {
		    return name;
		  }
		  public int getAge() {
		    return age;
		  }

		  @Override
		  public String toString() {
		    return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
		  }
	}

}
