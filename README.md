# apigee-jms

This is a repository with sample code for JMS use cases with Apigee.

Target Integration (Southbound)

This is the most common use case for JMS integration. Implement a Java callout policy within the Apigee API proxy and attach it to end of the request pipeline.

Java callout class needs to implement com.apigee.flow.execution.spi.Execution interface. This interface has only one method viz. public ExecutionResult execute(MessageContext messageContext, ExecutionContext executionContext). Implement JMS Queue Sender code in the “execute” method. Return com.apigee.flow.execution.ExecutionResult.ABORT status from the method in case of any exception while processing the request. Return com.apigee.flow.execution.ExecutionResult.SUCCESS when the message is successfully sent to the JMS queue.

Southbound JMS integration works only with private cloud Apigee Edge. It is not supported in Apigee Cloud.

Please find a sample Java callout policy code here https://github.com/Abdul-Kinadiyil/apigee-jms/tree/master/southbound

Client Side Integration (Northbound)

Typical use case would be an application listening on a JMS queue and invoking an API on Apigee Edge upon receiving a message from the queue. JMS queue receiver needs to be implemented outside the Apigee Edge platform. It can be implemented as a Spring Boot application and deployed on a Virtual Machine or a Docker container. In Google Cloud platform, JMS receiver application can be deployed as a Google App Engine Flexible Environment application.

Please find a sample Spring Boot JMS receiver application that subscribes to Websphere MQ queue here https://github.com/Abdul-Kinadiyil/apigee-jms/tree/master/northbound

This code can be easily extended to consume messages from other JMS providers. Define ConnectionFactory with properties to connect to the specific JMS provider and create JMSListenerContainerFactory instance using the ConnectionFactory object in main.java.receiver.Application Class.

JMS message consumer class (main.java.receiver.JMSReceiver) has one method receiveMessage(Message message) that subscribes message from the queue specified as the “destination” in the method annotation. Implement additional method in the class for each queue that you need to subscribe similar to the receiveMessage method. Implement MessageHandler class to process each JMS message type and call it from the JMSReceiver class method. JMS provider connection information and API urls should be configured in the application.properties file.
This is a repo with sample code for target and client side JMS integration of APIs using Apigee Edge.
