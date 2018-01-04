package main.java.receiver;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import main.java.receiver.apibeans.*;

@Component
public class MessageHandler {

	private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    private Client restClient;
    
    @Autowired
    private GlobalProperties properties;
	
	public MessageHandler()
	{
		init();
	}
	
	private void init()
	{
			try
			{
			logger.debug("Creating REST client");
			DefaultClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
					// Creates client and adds authentication filter
			restClient = Client.create(clientConfig);
			}
			catch(Exception e)
			{
				logger.error("Exception occurred while initializing REST client", e);
			}
	}
	
	public void processResponseMsg(String msg)
	{
		try
		{

			String url = properties.getApihost();
			url += properties.getApiUrl();	
			logger.info("API url=="+url);
			processMessage(url,msg);
		
		}catch(Exception e)
		{
			logger.error("Exception occurred while processing Response Message for API", e);
			
		}
	}
	
		public void processMessage(String url, String msg)
		{
			WebResource webResource = restClient.resource(url);
	        	APIResponse mresponse = webResource.accept(MediaType.APPLICATION_JSON)
	        				.header("apikey", "apikey")
	        				.entity(msg, MediaType.TEXT_PLAIN_TYPE)
						.post(APIResponse.class);
	        	if(mresponse.getMessage().equals("OK"))
	    	   		logger.info("Successfully Posted message to API==" + mresponse.getMessage());
	        	else
	    	   		logger.info("Problem while sending message to API" + mresponse.getMessage());
		}
}		
		
	
	
	
	

