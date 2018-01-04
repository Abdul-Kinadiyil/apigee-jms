package main.java.receiver;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.context.annotation.PropertySource;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@SpringBootApplication
@EnableJms
@PropertySource(value={"classpath:application.properties"})
public class Application {

	 @Value("${host}")
	    private String host;
	    @Value("${port}")
	    private Integer port;
	    @Value("${queuemanager}")
	    private String queueManager;
	    @Value("${channel}")
	    private String channel;
	    @Value("${username}")
	    private String username;
	    @Value("${password}")
	    private String password;
	    /*
	    @Value("${queue}")
	    private String queue;
	    */
	    @Value("${receive-timeout}")
	    private long receiveTimeout;
	    
	    private static Logger logger = LoggerFactory.getLogger(Application.class);

	    @Bean
	    public MQQueueConnectionFactory mqQueueConnectionFactory() {
	        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
	        mqQueueConnectionFactory.setHostName(host);
	        try {
	            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
	            mqQueueConnectionFactory.setCCSID(1208);
	            mqQueueConnectionFactory.setChannel(channel);
	            mqQueueConnectionFactory.setPort(port);
	            mqQueueConnectionFactory.setQueueManager(queueManager);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mqQueueConnectionFactory;
	    }

	    @Bean
	    UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(MQQueueConnectionFactory mqQueueConnectionFactory) {
	        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
	        userCredentialsConnectionFactoryAdapter.setUsername(username);
	        userCredentialsConnectionFactoryAdapter.setPassword(password);
	        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
	        return userCredentialsConnectionFactoryAdapter;
	    }

	    @Bean
	    @Primary
	    public CachingConnectionFactory cachingConnectionFactory(UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
	        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
	        cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
	        cachingConnectionFactory.setSessionCacheSize(500);
	        cachingConnectionFactory.setReconnectOnException(true);
	        return cachingConnectionFactory;
	    }
    @Bean
    public JmsListenerContainerFactory<?> myFactory(CachingConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
    
    @Bean
    public GlobalProperties getPropertyInstance()
    {
            return new GlobalProperties();
    }


    public static void main(String[] args) {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        logger.info("***JMS Receiver Application started****");
        
    }

}
