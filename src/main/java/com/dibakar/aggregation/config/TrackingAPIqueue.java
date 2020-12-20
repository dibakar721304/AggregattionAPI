package com.dibakar.aggregation.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class TrackingAPIqueue {
	public static final String TRACKING_QUEUE = "trackingAPI.queue";
	public static final String TRACKING_BROKER_URL = "tcp://localhost:61616";

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(TRACKING_BROKER_URL);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplateTracking() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
		jmsTemplate.setDefaultDestinationName(TRACKING_QUEUE);
		return jmsTemplate;
	}

}
