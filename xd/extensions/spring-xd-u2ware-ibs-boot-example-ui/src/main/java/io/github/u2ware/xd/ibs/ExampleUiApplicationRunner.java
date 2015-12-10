package io.github.u2ware.xd.ibs;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class ExampleUiApplicationRunner implements InitializingBean, DisposableBean {
	
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
		
		System.out.println("afterPropertiesSet... 222");

		System.setProperty(ExampleUiApplication.class.getName(), ""+httpPort);
		String[] args = new String[]{};

		//String[] args = new String[]{"--server.port="+httpPort};

		context = SpringApplication.run(ExampleUiApplication.class, args);
		
		System.out.println("afterPropertiesSet... "+httpPort);
	}
}
