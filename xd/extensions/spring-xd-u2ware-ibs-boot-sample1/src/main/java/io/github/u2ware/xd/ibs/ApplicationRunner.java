package io.github.u2ware.xd.ibs;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class ApplicationRunner implements InitializingBean, DisposableBean {
	
	private ApplicationContext context;
	
	private int httpPort;
	
	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	@Override
	public void destroy() throws Exception {
		SpringApplication.exit(context);
		System.out.println("ExampleUiApplicationRunner exit: port="+httpPort);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		System.out.println("ExampleUiApplicationRunner ready: port="+httpPort);

		System.setProperty(Application.class.getName(), ""+httpPort);
		String[] args = new String[]{};

		//String[] args = new String[]{"--server.port="+httpPort};

		context = SpringApplication.run(Application.class, args);
		
		System.out.println("ExampleUiApplicationRunner started: port="+httpPort);
	}
}
