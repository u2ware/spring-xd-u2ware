package io.github.u2ware.xd.ibs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

@SpringBootApplication(exclude={JmxAutoConfiguration.class, IntegrationAutoConfiguration.class})
public class MongodbRestApplication {

	public static void main(String[] args) {
        SpringApplication.run(MongodbRestApplication.class, args);
    }
}
