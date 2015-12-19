package io.github.u2ware.xd.ibs;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringBootApplicationRunner implements InitializingBean, DisposableBean {
	
	private final Log logger = LogFactory.getLog(getClass());

	private ApplicationContext context;
	private Class<?> configClass;
	private Map<String, Object> defaultProperties;
	
	public void setConfigClass(Class<?> configClass) {
		this.configClass = configClass;
	}
	public void setDefaultProperties(Map<String, Object> defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	@Override
	public void destroy() throws Exception {
		SpringApplication.exit(context);
		logger.info("terminated");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		logger.info("initialize: "+defaultProperties);
		if(defaultProperties != null){
			for(String key : defaultProperties.keySet()){
				System.setProperty(getClass().getName()+"#"+key, defaultProperties.get(key).toString());
			}
		}
		
		SpringApplication application = new SpringApplication(configClass);
		application.setDefaultProperties(defaultProperties);
		context = application.run(new String[]{});
		
		logger.info("started");
	}
}
