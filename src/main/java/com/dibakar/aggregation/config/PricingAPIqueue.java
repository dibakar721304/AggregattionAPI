package com.dibakar.aggregation.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class PricingAPIqueue {
	public static final String PRICING_QUEUE = "pricingAPI.queue";
	public static final String PRICING_BROKER_URL = "tcp://localhost:61616";

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(PRICING_BROKER_URL);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplatePricing() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
		jmsTemplate.setDefaultDestinationName(PRICING_QUEUE);
		return jmsTemplate;
	}

}
