package io.github.u2ware.xd.mongodb;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongodbServer {

	protected static MongodExecutable _mongodExe;
	protected static MongodProcess _mongod;

	public static void main(String[] args) throws Exception{
		
		int port = 27017;
		try{
			port = Integer.parseInt(args[0]);
		}catch(Exception e){
		}
		startup(port);
	}
	
	public static void startup(int port) throws Exception{
		_mongodExe =  MongodStarter.getDefaultInstance()
				.prepare(new MongodConfigBuilder()
						.version(Version.Main.PRODUCTION)
						.net(new Net(port, Network.localhostIsIPv6()))
						.build()
				);
		_mongod = _mongodExe.start();
	}
	
	public static void shutdown() throws Exception{
		_mongod.stop();
		_mongodExe.stop();
	}	
	
	
	public static class Sample {

		private String id;
		private Object value;
		private Object others;
		
		public Sample(){

		}
		public Sample(String id, Object value, Object others){
			this.id = id;
			this.value = value;
			this.others = others;
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
		public Object getOthers() {
			return others;
		}
		public void setOthers(Object others) {
			this.others = others;
		}
		
		
		
	}
	
}
