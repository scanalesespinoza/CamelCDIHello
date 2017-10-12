package com.redhat.ws.proxy;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.properties.PropertiesComponent;
import javax.jms.Session;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;

@ContextName("proxy-context")
public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().component("jetty").dataFormatProperty("prettyPrint", "true").port(9080)
				.contextPath("/rest-proxy/").enableCORS(true).corsHeaderProperty("Access-Control-Allow-Headers",
						"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,CustomHeader1, CustomHeader2");

		PropertiesComponent pc = new PropertiesComponent();
		pc.setLocation("classpath:properties/endpoint.properties");
		pc.setSystemPropertiesMode(2);
		getContext().addComponent("properties", pc);

		// This component is for simply reading and writing to Queues
//		getContext().addComponent("activemqSimple", getActiveMQSimpleComponent());
		// It used connection pooling to listen to Topic
//    		getContext().addComponent("activemqPool", getActiveMQPoolComponent());

		onException(Exception.class).to("log:GeneralError?level=ERROR").log("${exception.stacktrace}");

		from("{{endpoint.from}}").convertBodyTo(String.class).to("velocity:velocity/insert_object.vm")
				.log("${body}");
//				.log("${header.breadcrumbId}")
//				.to("log:com.redhat.ws.proxy?showHeaders=true")
				//.setHeader("JMSCorrelationID",simple("${header.breadcrumbId}"))
//				.setHeader("JMSMessageID",simple("${header.breadcrumbId}"))
//				.to("{{endpoint.to}}")
//				.to("log:com.redhat.ws.proxy?level=DEBUG&showAll=true");

	}
/*
  public ActiveMQConnectionFactory getConnectionFactory() {
    return new ActiveMQConnectionFactory("tcp://scanales.t420:61616");
  }

  // This component will be used for simple activemq connection
  public ActiveMQComponent getActiveMQSimpleComponent() {
    return ActiveMQComponent.activeMQComponent("tcp://scanales.t420:61616");
  }

  // This component will be used for connection pooling
  public ActiveMQComponent getActiveMQPoolComponent() {
    ActiveMQComponent activeMQComponent = new ActiveMQComponent();
    activeMQComponent.setConfiguration(getJmsConfiguration());
    return activeMQComponent;
  }

  public PooledConnectionFactory getPooledConnectionFactory() {
    PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
    // A maximum of 10 connections can be opened on high volume of messages
    pooledConnectionFactory.setMaxConnections(10);
    pooledConnectionFactory.setConnectionFactory(getConnectionFactory());
    return pooledConnectionFactory;
  }

  public JmsConfiguration getJmsConfiguration() {
    JmsConfiguration jmsConfiguration = new JmsConfiguration();
    // Once all the messages are sent or received, the client send
    // acknowledgement to ActiveMQ
    jmsConfiguration.setAcknowledgementMode(Session.AUTO_ACKNOWLEDGE);
    jmsConfiguration.setTransacted(false);
    // It will start at 3 parallel consumers
    jmsConfiguration.setConcurrentConsumers(3);
    jmsConfiguration.setConnectionFactory(getPooledConnectionFactory());
    return jmsConfiguration;
  }
  */
}
