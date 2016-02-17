package io.github.u2ware.xd.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.hornetq.HornetQAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceDelegatingViewResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration;
import org.springframework.boot.autoconfigure.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration;
import org.springframework.boot.autoconfigure.social.LinkedInAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;

@SpringBootApplication(exclude={
		org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportProperties.class,
		org.springframework.boot.actuate.autoconfigure.EndpointWebMvcAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.EndpointWebMvcChildContextConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfigurationProperties.class,
		org.springframework.boot.actuate.autoconfigure.HealthMvcEndpointProperties.class,
		org.springframework.boot.actuate.autoconfigure.JolokiaAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.JolokiaProperties.class,
		org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.ManagementServerProperties.class,
		org.springframework.boot.actuate.autoconfigure.ManagementSecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.PublicMetricsAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.ShellProperties.class,
		org.springframework.boot.actuate.autoconfigure.TraceRepositoryAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.TraceWebFilterAutoConfiguration.class,
		
		
		ActiveMQAutoConfiguration.class,
/*		AopAutoConfiguration.class,*/
		//ArtemisAutoConfiguration.class,
		BatchAutoConfiguration.class,
		//CacheAutoConfiguration.class,
		//CassandraAutoConfiguration.class,
		//CassandraDataAutoConfiguration.class,
		//CassandraRepositoriesAutoConfiguration.class,
		CloudAutoConfiguration.class,
		//ConfigurationPropertiesAutoConfiguration.class,
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		DeviceDelegatingViewResolverAutoConfiguration.class,
		DeviceResolverAutoConfiguration.class,
/*		DispatcherServletAutoConfiguration.class,*/
		ElasticsearchAutoConfiguration.class,
		ElasticsearchDataAutoConfiguration.class,
		ElasticsearchRepositoriesAutoConfiguration.class,
		//EmbeddedMongoAutoConfiguration.class,
/*		EmbeddedServletContainerAutoConfiguration.class,*/
/*		ErrorMvcAutoConfiguration.class,*/
		FacebookAutoConfiguration.class,
		FallbackWebSecurityAutoConfiguration.class,
		FlywayAutoConfiguration.class,
		FreeMarkerAutoConfiguration.class,
		GroovyTemplateAutoConfiguration.class,
		GsonAutoConfiguration.class,
		//H2ConsoleAutoConfiguration.class,
		//HazelcastAutoConfiguration.class,
		//HazelcastJpaDependencyAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		HornetQAutoConfiguration.class,
/*		HttpEncodingAutoConfiguration.class,*/
/*		HttpMessageConvertersAutoConfiguration.class,*/
		HypermediaAutoConfiguration.class,
		IntegrationAutoConfiguration.class,
/*		JacksonAutoConfiguration.class,*/
		JerseyAutoConfiguration.class,
		JmsAutoConfiguration.class,
		JmxAutoConfiguration.class,
		JndiConnectionFactoryAutoConfiguration.class,
		JndiDataSourceAutoConfiguration.class,
		//JooqAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		JtaAutoConfiguration.class,
		LinkedInAutoConfiguration.class,
		LiquibaseAutoConfiguration.class,
		MailSenderAutoConfiguration.class,
		//MailSenderValidatorAutoConfiguration.class,
/*		MessageSourceAutoConfiguration.class,*/
/*		MongoAutoConfiguration.class,*/
/*		MongoDataAutoConfiguration.class,*/
/*		MongoRepositoriesAutoConfiguration.class,*/
		MultipartAutoConfiguration.class,
		MustacheAutoConfiguration.class,
		//OAuth2AutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class,
/*		PropertyPlaceholderAutoConfiguration.class,*/
		RabbitAutoConfiguration.class,
		ReactorAutoConfiguration.class,
		RedisAutoConfiguration.class,
		RepositoryRestMvcAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		//SecurityFilterAutoConfiguration.class,
		//SendGridAutoConfiguration.class,
		ServerPropertiesAutoConfiguration.class,
		//SessionAutoConfiguration.class,
		SitePreferenceAutoConfiguration.class,
		SocialWebAutoConfiguration.class,
		SolrAutoConfiguration.class,
		SolrRepositoriesAutoConfiguration.class,
		//SpringApplicationAdminJmxAutoConfiguration.class,
/*		SpringDataWebAutoConfiguration.class,*/
		ThymeleafAutoConfiguration.class,
		//TransactionAutoConfiguration.class,
		TwitterAutoConfiguration.class,
		VelocityAutoConfiguration.class,
/*		WebMvcAutoConfiguration.class,*/
		WebSocketAutoConfiguration.class,
		//WebSocketMessagingAutoConfiguration.class,
		XADataSourceAutoConfiguration.class,
})
public class MongodbRestApplication {
	
	//DispatcherServletAutoConfiguration f;
	
	public static void main(String[] args) {
        SpringApplication.run(MongodbRestApplication.class, args);
    }
	/*
	@Value("#{systemProperties['io.github.u2ware.integration.netty.x.SpringBootEmbeddedServer#server.port']}")
	private Integer value;

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if(value != null){
			container.setPort(value);
		}
	}
	*/
}
