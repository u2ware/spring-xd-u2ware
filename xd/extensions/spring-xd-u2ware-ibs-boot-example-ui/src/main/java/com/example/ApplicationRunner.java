package com.example;

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
		System.out.println("destroy... "+httpPort);
		SpringApplication.exit(context);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		System.out.println("afterPropertiesSet... ");

		String[] args = new String[]{"--server.port="+httpPort};
		context = SpringApplication.run(Application.class, args);

		System.out.println("afterPropertiesSet... "+httpPort);
	}
}
