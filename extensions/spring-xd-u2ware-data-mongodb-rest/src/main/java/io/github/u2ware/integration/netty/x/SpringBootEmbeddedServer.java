package io.github.u2ware.integration.netty.x;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringBootEmbeddedServer implements InitializingBean, DisposableBean {
	
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
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		String[] args = null;
		if(defaultProperties != null){
			args = new String[defaultProperties.size()];
			int i = 0; 
			for(String key : defaultProperties.keySet()){
				args[i] = new StringBuilder()
					.append("--")
					.append(key)
					.append("=")
					.append(defaultProperties.get(key))
					.toString();
				i++;
			}
		}else{
			args = new String[]{"--server.port=8080"};
		}
		
		SpringApplication application = new SpringApplication(configClass);
		application.setShowBanner(false);
		application.setDefaultProperties(defaultProperties);
		context = application.run(args);

		if(logger.isDebugEnabled()){
			String[] beanNames = context.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for(String beanName : beanNames){
				Object beanObject = context.getBean(beanName);
				if(beanObject != null){
					logger.debug(beanName+"="+beanObject.getClass());
				}else{
					logger.debug(beanName+"="+null);
				}
			}
		}
	}
	
}
