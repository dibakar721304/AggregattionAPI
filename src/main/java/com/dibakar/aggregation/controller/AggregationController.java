package com.dibakar.aggregation.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.model.APIConfig;
import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.repository.APIConfigRepository;
import com.dibakar.aggregation.services.AggregationServices;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(Constant.FORWARD_SLASH)
public class AggregationController {

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
	public @NotNull TNTaggregation getAggregateResponse(@RequestParam Map<String, String> queryParams) {
		return aggregationServices.getAggregateResponse(queryParams);
	}

	@GetMapping(Constant.SHIPMENTS)
	public Map<String, List<String>> getSipmentResponse(@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam)
			throws JsonParseException, JsonMappingException, IOException {
		jmsTemplateShipments.convertAndSend(jmsTemplateShipments.getDefaultDestinationName(), queryParam);
		APIConfig apiConfig = apiConfigRepository.findById(1).orElse(null);
		if (apiConfig.getRequest_count()>0 && apiConfig.getRequest_count()%5==0) {
			return aggregationServices.getSipmentResponse(StringUtils.chop(apiConfig.getQuery_param()));
		}
		return null;

	}

	@GetMapping(Constant.TRACKING)
	public Map<String, String> getTrackingResponse(@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam)
			throws JsonParseException, JsonMappingException, IOException {
		jmsTemplateTracking.convertAndSend(jmsTemplateTracking.getDefaultDestinationName(), queryParam);
		APIConfig apiConfig = apiConfigRepository.findById(2).orElse(null);
		if (apiConfig.getRequest_count()>0 && apiConfig.getRequest_count()%5==0) {
			return aggregationServices.getTrackingResponse(StringUtils.chop(apiConfig.getQuery_param()));
		}
		return null;
	}

	@GetMapping(Constant.PRICING)
	public Map<String, Float> getPricingResponse(@RequestParam(Constant.QUERY_PARAM_STRING) String queryParam)
			throws JsonParseException, JsonMappingException, IOException, JmsException, JMSException {
		jmsTemplatePricing.convertAndSend(jmsTemplatePricing.getDefaultDestinationName(), queryParam);
		APIConfig apiConfig = apiConfigRepository.findById(1).orElse(null);
		if (apiConfig.getRequest_count()>0 && apiConfig.getRequest_count()%5==0) {
			return aggregationServices.getPricingResponse(StringUtils.chop(apiConfig.getQuery_param()));
		}
		return null;
	}

}
