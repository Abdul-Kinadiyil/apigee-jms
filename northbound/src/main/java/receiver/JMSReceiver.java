package main.java.receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jms.Message;
import javax.jms.TextMessage;
@Component
@PropertySource(value={"classpath:application.properties"})
public class JMSReceiver {
	 private static Logger logger = LoggerFactory.getLogger(JMSReceiver.class);
	
	 @Autowired
     private MessageHandler messageHandler;
	 
	 
	 @JmsListener(destination = "queue name", containerFactory = "myFactory")
	    public void receiveMessage(Message message) {
	    	
	    	if (message instanceof TextMessage)
	        {
	            try
	            {
	                String msg = ((TextMessage) message).getText();
	                if(message.getJMSReplyTo()!= null)
	                		logger.debug("Reply to===="+ message.getJMSReplyTo().toString());
	                logger.debug("Dequeued from queue, Message=="+msg );
	                messageHandler.processResponseMsg(msg);;
	            }
	            catch (Exception e)
	            {
	               logger.error("Exception occured in JMSReceiver::", e);
	               
	            }
	            
	        }
		}
	 
	

}