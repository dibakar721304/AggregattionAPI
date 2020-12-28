package com.dibakar.aggregation.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.repository.APIConfigRepository;
import com.dibakar.aggregation.services.AggregationServices;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The controller class for receiving requests
 * 
 * @author Dibakar
 *
 */
@RestController
@RequestMapping(Constant.FORWARD_SLASH)
public class AggregationController {
	Logger logger = LoggerFactory.getLogger(AggregationController.class);

	@Autowired
	JmsTemplate jmsTemplatePricing;
	@Autowired
	JmsTemplate jmsTemplateTracking;
	@Autowired
	JmsTemplate jmsTemplateShipments;

	@Autowired
	private AggregationServices aggregationServices;
	@Autowired
	APIConfigRepository apiConfigRepository;

	@CrossOrigin
	@GetMapping(Constant.AGGREGATION)
	public ResponseEntity<TNTaggregation> getAggregateResponse(@RequestParam Map<String, String> queryParams) {
		logger.debug("Query params for aggregation API" + queryParams);
		TNTaggregation tntAggregation = aggregationServices.getAggregateResponse(queryParams);
		if (null == tntAggregation) {
			logger.error("aggregation response for  :" + queryParams + " Not Found!");
			throw new com.dibakar.aggregation.exception.ResourceNotFoundException(
					"aggregation details for  :" + queryParams + " Not Found!");
		}
		return ResponseEntity.ok().body(tntAggregation);
	}

	@GetMapping(Constant.SHIPMENTS)
	public ResponseEntity<Map<String, List<String>>> getShipmentResponse(
			@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam) {
		logger.debug("Query params for Shipments API" + queryParam);

		Map<String, List<String>> shipmentResponse = aggregationServices.getShipmentResponse(queryParam);
		if (null == shipmentResponse) {
			logger.error("shipment details for  :" + queryParam + " Not Found!");
			throw new com.dibakar.aggregation.exception.ResourceNotFoundException(
					"shipment details for  :" + queryParam + " Not Found!");
		}
		return ResponseEntity.ok().body(shipmentResponse);

	}

	@GetMapping(Constant.TRACKING)
	public ResponseEntity<Map<String, String>> getTrackingResponse(
			@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam)
			throws JsonParseException, JsonMappingException, IOException {
		logger.debug("Query params for Tracking API" + queryParam);

		Map<String, String> trackingResponse = aggregationServices.getTrackingResponse(queryParam);
		if (null == trackingResponse) {
			logger.error("tracking details for  :" + queryParam + " Not Found!");
			throw new com.dibakar.aggregation.exception.ResourceNotFoundException(
					"tracking details for  :" + queryParam + " Not Found!");
		}

		return ResponseEntity.ok().body(trackingResponse);

	}

	@GetMapping(Constant.PRICING)
	public ResponseEntity<Map<String, Float>> getPricingResponse(
			@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam) {
		logger.debug("Query params for Tracking API" + queryParam);

		Map<String, Float> pricingResponse = aggregationServices.getPricingResponse(queryParam);
		if (null == pricingResponse) {
			logger.error("pricing details for  :" + queryParam + " Not Found!");
			throw new com.dibakar.aggregation.exception.ResourceNotFoundException(
					"pricing details for  :" + queryParam + " Not Found!");
		}

		return ResponseEntity.ok().body(pricingResponse);
	}

}
