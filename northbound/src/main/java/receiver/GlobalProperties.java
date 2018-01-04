package main.java.receiver;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;


@PropertySource("classpath:application.properties")
public class GlobalProperties {
	
	    private String host;

	    private Integer port;
	    
	    private String queuemanager;

	    private String channel;

	    private String username;

	    private String password;
		
		@Value("${apiurl}")
	    private String apiurl;

		@Value("${jmsingressurl}")
	    private String jmsingressurl;
		
		@Value("${apihost}")
		private String apihost;
		
		public String getApiUrl() {
			return apiurl;
		}

		public void setApiUrl(String apiurl) {
			this.apiurl = apiurl;
		}
				
		public String getApihost() {
			return apihost;
		}

		public void setApihost(String apihost) {
			this.apihost = apihost;
		}

		@Bean
		public static GlobalProperties getInstance()
		{
			return new GlobalProperties();
		}

		public String getJmsingressurl() {
			return jmsingressurl;
		}

		public void setJmsingressurl(String jmsingressurl) {
			this.jmsingressurl = jmsingressurl;
		}

		
		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getQueuemanager() {
			return queuemanager;
		}

		public void setQueuemanager(String queuemanager) {
			this.queuemanager = queuemanager;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

    //getters and setters

}
