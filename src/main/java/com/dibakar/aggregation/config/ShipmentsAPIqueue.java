package com.dibakar.aggregation.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ShipmentsAPIqueue {
	public static final String SHIPMENTS_QUEUE = "shipmentsAPI.queue";
	public static final String SHIPMENTS_BROKER_URL = "tcp://localhost:61616";

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(SHIPMENTS_BROKER_URL);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplateShipments() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
		jmsTemplate.setDefaultDestinationName(SHIPMENTS_QUEUE);
		return jmsTemplate;
	}

}
