package io.github.u2ware.xd.ibs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

@SpringBootApplication
public class Application {
/*	
	implements EmbeddedServletContainerCustomizer {

	@Value("#{systemProperties['io.github.u2ware.xd.ibs.Application']}")
	private Integer httpPort = 9898;
	
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(httpPort);
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //JettyEmbeddedServletContainer f;
    }
}
