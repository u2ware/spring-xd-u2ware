package io.github.u2ware.xd.ibs;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringBootRunner implements InitializingBean, DisposableBean {
	
	private ApplicationContext context;
	
	private int httpPort;
	private Class<?> configClass;
	
	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public Class<?> getConfigClass() {
		return configClass;
	}

	public void setConfigClass(Class<?> configClass) {
		this.configClass = configClass;
	}

	@Override
	public void destroy() throws Exception {
		SpringApplication.exit(context);
		System.out.println("SpringBootRunner exit: port="+httpPort);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		System.out.println("SpringBootRunner ready: port="+httpPort);

		//System.setProperty(SpringBootRunner.class.getName(), ""+httpPort);
		//String[] args = new String[]{};
		String[] args = new String[]{"--server.port="+httpPort};
		context = SpringApplication.run(getConfigClass(), args);
		
		System.out.println("SpringBootRunner started: port="+httpPort);
	}
}
