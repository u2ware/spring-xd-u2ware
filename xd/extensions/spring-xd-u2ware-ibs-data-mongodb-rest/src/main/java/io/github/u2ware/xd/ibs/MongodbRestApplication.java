package io.github.u2ware.xd.ibs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongodbRestApplication {

	/*
	implements EmbeddedServletContainerCustomizer{

	@Value("#{systemProperties['MongodbRestApplicationRunner.server.port']}")
	private Integer port;
	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(port);
	}
	*/

	public static void main(String[] args) {
        SpringApplication.run(MongodbRestApplication.class, args);
    }
}
