package com.dibakar.aggregation.listener;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dibakar.aggregation.exception.ResourceNotFoundException;
import com.dibakar.aggregation.model.APIConfig;
import com.dibakar.aggregation.repository.APIConfigRepository;
/**
 * Queue listener for shipments API
 * @author Dibakar
 *
 */
@Component
public class ShipmentsRequestListener {
	Logger logger = LoggerFactory.getLogger(ShipmentsRequestListener.class);

	@Autowired
	APIConfigRepository apiConfigRepository;
	private boolean isOkToProcessWithinFiveSec;

	@JmsListener(destination = "shipmentsAPI.queue")
	public void consume(String message)  {
		APIConfig apiConfig = apiConfigRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException(
				"While consuming shipments queue, API with ID :" + 1 + " Not Found!"));

		if (apiConfig.getRequest_count() == 0) {
			apiConfig.setFirst_request_timestamp(LocalDateTime.now());
		}
		apiConfig.setRequest_count(apiConfig.getRequest_count() + 1);
		apiConfig.setQuery_param(apiConfig.getQuery_param() + message + ",");

		apiConfigRepository.save(apiConfig);
		if (apiConfig.getRequest_count() == 5) {
			return;
		}
		try {
			while (!isOkToProcessWithinFiveSec) {
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			logger.error("Encountered exception while consuming shipments queue");
		}

	}
	/**
	 * Method to check condition for clearing the pricing queue.
	 * @return
	 * @throws InterruptedException
	 */
	@Scheduled(fixedRate = 5000)
	public boolean isOkToClearQueue() throws InterruptedException {
		logger.debug("shipment queue scheduler started at" + LocalDateTime.now());

		APIConfig apiConfig = apiConfigRepository.findById(1).orElse(null);
		Thread.sleep(100);
		if (null != apiConfig && apiConfig.getRequest_count() > 0 && apiConfig.getRequest_count() != 5
				&& LocalDateTime.now().isBefore(apiConfig.getFirst_request_timestamp().plusSeconds(5))) {
			isOkToProcessWithinFiveSec = true;
		}
		logger.debug("shipment queue scheduler ended at" + LocalDateTime.now());

		return isOkToProcessWithinFiveSec;
	}

}
