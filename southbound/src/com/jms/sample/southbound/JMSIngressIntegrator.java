package com.jms.sample.southbound;

import com.apigee.flow.execution.ExecutionContext;

import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.Message;
import com.apigee.flow.message.MessageContext;
import org.apache.log4j.Logger;
import com.ibm.msg.client.wmq.*;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.headers.CCSID;
import com.ibm.mq.jms.*;
import javax.jms.*;

import java.io.StringWriter;
import java.io.PrintWriter;

import java.util.Map;

public class JMSIngressIntegrator implements Execution {

	protected static Logger logger = Logger.getLogger(JMSIngressIntegrator.class);

	private Map <String, String> properties;

	public JMSIngressIntegrator (Map <String, String> properties) {
		this.properties = properties;
	}
	public JMSIngressIntegrator()
	{
		
	}
	
	public ExecutionResult execute(MessageContext messageContext, ExecutionContext executionContext)
    {
		String content = null;
		Session session = null;
		MessageProducer producer = null;
		QueueConnection qc = null;
		String queueManager = null;
		String queue = null;
		String channel = null;
		String user = null;
		String pwd = null;
		String host = null;
		String port = null;
		String replytoqueue = "";


		String debug = "";
		try
        {
			if(logger.isDebugEnabled())
                logger.debug("JMSIngressIntegrator.execute() Method entry");
			
			queueManager = (String)messageContext.getVariable("queueManager");
			queue = (String)messageContext.getVariable("queue");
			port = (String)messageContext.getVariable("port");
			user = (String)messageContext.getVariable("user");
			pwd = (String)messageContext.getVariable("pwd");
			host = (String)messageContext.getVariable("host");
			channel = (String)messageContext.getVariable("channel");
			replytoqueue = (String)messageContext.getVariable("replytoqueue");
			debug += "JMS Integrator\n";
			debug += "  Queue manager: " + queueManager + " \n";
			debug += "  Queue: " + queue + " \n";
			debug += "  Channel: " + channel + " \n";
			debug += "  User: " + user + " \n";
			debug += "  host: " + host + " \n";			
			debug += "  replytoqueue: " + replytoqueue + " \n";	
			
			MQQueueConnectionFactory qcf = new MQQueueConnectionFactory();
			qcf.setHostName(host);
			qcf.setPort(Integer.parseInt(port));
			qcf.setQueueManager(queueManager);
			qcf.setChannel(channel);
			qcf.setTransportType (WMQConstants.WMQ_CM_CLIENT);
			qc = qcf.createQueueConnection(user, pwd);

			qc.start();
			debug += "Connected to " + host + ":" + port + " \n";


			// Creating session for sending messages
			session = qc.createSession(false, Session.AUTO_ACKNOWLEDGE);
			  
			// Getting the queue
			//Destination destination = session.createQueue(queue);
			Queue myQueue = session.createQueue("queue:///" + queue + "?targetClient=1");
			//myQueue.setTargetClient(WMQConstants.WMQ_TARGET_DEST_MQ);
		
			// MessageProducer is used for sending messages (as opposed
			// to MessageConsumer which is used for receiving them)
			producer = session.createProducer(myQueue);
			
			content = messageContext.getMessage().getContent();
			TextMessage message = session.createTextMessage(content);
		
			/*
			byte[] data = (byte[])messageContext.getVariable("ebcdic");
			BytesMessage message = session.createBytesMessage();
			message.setIntProperty(WMQConstants.JMS_IBM_CHARACTER_SET, 37);
			message.writeBytes(data);
			*/
			if(replytoqueue != null)
			{
				Destination replToQ = session.createQueue(replytoqueue);
				message.setJMSReplyTo(replToQ);
				debug += "ReplyToQueue set to " + replytoqueue + " \n";

			}
			debug += content + "\n";
			   
			producer.send(message);

			debug += "Message sent\n";

			messageContext.setVariable("javaDebug", debug);            
        }
        catch(Exception exception)
        {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);

			String sStackTrace = sw.toString(); 

            logger.error(exception);
            
            messageContext.setVariable("javaDebug", debug);
            messageContext.setVariable("javaStackTrace", sStackTrace);

            return ExecutionResult.ABORT;
        }
		finally
		{
			destroyQueueTransport(session,producer,qc);
		}
        return ExecutionResult.SUCCESS;
    }
	
	protected  void destroyQueueTransport(Session session,MessageProducer producer,QueueConnection qc){
        if(producer!=null){
            try{
            	producer.close();
            }
            catch(Exception e){}
            producer=null;
        }
        if(session!=null){
            try{
            	session.close();
            }
            catch(Exception e){}
            session=null;
        }
        if(qc!=null){
            try{
            	qc.close();
            }
            catch(Exception e){}
            qc=null;
        }
    }	
}