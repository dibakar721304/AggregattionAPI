package com.dibakar.aggregation.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.dibakar.aggregation.constant.Constant;
/**
 * Queue configuration class for tracking API
 * @author Dibakar
 *
 */
@Configuration
public class TrackingAPIqueue {
	Logger logger = LoggerFactory.getLogger(TrackingAPIqueue.class);

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(Constant.ACTIVEMQ_BROKER_URL);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplateTracking() {
		logger.debug("jmsTemplateTracking() started");
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
		jmsTemplate.setDefaultDestinationName(Constant.TRACKING_QUEUE);
		logger.debug("jmsTemplateTracking() ended");
		return jmsTemplate;
	}

}
