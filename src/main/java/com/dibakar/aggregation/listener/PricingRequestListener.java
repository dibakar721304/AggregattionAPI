package com.dibakar.aggregation.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.dibakar.aggregation.model.APIConfig;
import com.dibakar.aggregation.repository.APIConfigRepository;

@Component
public class PricingRequestListener {
	List<String> messageList = new ArrayList<String>();
	@Autowired
	APIConfigRepository apiConfigRepository;

	@JmsListener(destination = "pricingAPI.queue")
	public void consume(String message) {
		System.out.println("Received Message: " + message);
		APIConfig apiConfig = apiConfigRepository.findById(3).orElse(null);
		apiConfig.setRequest_count(apiConfig.getRequest_count() + 1);
		apiConfig.setQuery_param(apiConfig.getQuery_param()+message+",");
		apiConfigRepository.save(apiConfig);
	}
}
