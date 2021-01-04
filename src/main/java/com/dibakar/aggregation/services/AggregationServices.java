package com.dibakar.aggregation.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.exception.MissingQueryParameterException;
import com.dibakar.aggregation.exception.ResourceNotFoundException;
import com.dibakar.aggregation.model.APIConfig;
import com.dibakar.aggregation.model.Pricing;
import com.dibakar.aggregation.model.Shipments;
import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.model.Tracking;
import com.dibakar.aggregation.repository.APIConfigRepository;
import com.dibakar.aggregation.repository.PricingRepository;
import com.dibakar.aggregation.repository.ShipmentsRepository;
import com.dibakar.aggregation.repository.TrackingRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class holds the business logic and integration with different layers
 * 
 * @author Dibakar
 *
 */
@Service
@Transactional
public class AggregationServices {
	Logger logger = LoggerFactory.getLogger(AggregationServices.class);

	@Autowired
	JmsTemplate jmsTemplatePricing;
	@Autowired
	JmsTemplate jmsTemplateTracking;
	@Autowired
	JmsTemplate jmsTemplateShipments;
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	@Autowired
	private APIConfigRepository apiConfigRepository;
	@Autowired
	private PricingRepository pricingRepository;
	@Autowired
	private ShipmentsRepository shipmentsRepository;
	@Autowired
	private TrackingRepository trackingRepository;

	/**
	 * This method populates the response from different services and bind it to
	 * TNTaggregation object
	 * 
	 * @param queryParam
	 * @return
	 * @throws JMSException
	 */
	public TNTaggregation getAggregateResponse(Map<String, String> queryParams) {
		logger.debug("getAggregateResponse() started");
		TNTaggregation tntAggregation = new TNTaggregation();
		updatedResponseBuilder();
		Map<String, List<String>> shipments = null;
		Map<String, String> tracking = null;
		Map<String, Float> pricing = null;

		shipments = getShipmentResponse(queryParams.get(Constant.SHIPMENTS));

		tracking = getTrackingResponse(queryParams.get(Constant.TRACKING));
		pricing = getPricingResponse(queryParams.get(Constant.PRICING));

		if (null != shipments && null != tracking && null != pricing) {
			tntAggregation.setPricing(pricing);
			tntAggregation.setTracking(tracking);
			tntAggregation.setShipments(shipments);
		}
		logger.debug("getAggregateResponse() ended");

		return tntAggregation;
	}

	/**
	 * This method return the response for shipments api
	 * 
	 * @param queryParam
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Map<String, List<String>> getShipmentResponse(String queryParam) {
		logger.debug("getShipmentResponse() started");
		APIConfig apiConfig = null;
		Map<String, List<String>> deserializeResponse = null;
		if (StringUtils.isEmpty(queryParam)) {
			throw new MissingQueryParameterException(
					"The shipment API request is invalid" + queryParam);
		}
		try {
			jmsTemplateShipments.convertAndSend(jmsTemplateShipments.getDefaultDestinationName(), queryParam);

			Thread.sleep(200);
			apiConfig = apiConfigRepository.findById(1).orElseThrow(
					() -> new ResourceNotFoundException("In getShipmentResponse(), API with ID :" + 1 + " Not Found!"));

			if (apiConfig.getRequest_count() > 0) {
				ResponseEntity<String> response = getResponseAsString(Constant.SHIPMENTS,
						StringUtils.chop(apiConfig.getQuery_param()));

				deserializeResponse = new ObjectMapper().readValue(response.getBody(),
						new TypeReference<Map<String, List<String>>>() {
						});

				saveShipmentsDetails(deserializeResponse);
				if (apiConfig.getRequest_count() == 5) {
					logger.info("End of cap for Shipments requests");
					updateRequestCountAndQueryParam(1);
				}
			}
		} catch (IOException | InterruptedException | JmsException e) {
			logger.error("Exception in getShipmentResponse() " + e.getLocalizedMessage());
		}
		logger.debug("getShipmentResponse() ended");

		return deserializeResponse;
	}

	/**
	 * Method to save shipment response objects in the DB
	 * 
	 * @param deserializeResponse
	 */
	private void saveShipmentsDetails(Map<String, List<String>> deserializeResponse) {
		logger.debug("saveShipmentsDetails() ended");

		List<Shipments> shipmentsList = new ArrayList<Shipments>();
		deserializeResponse.forEach((k, v) -> {
			Shipments shipments = new Shipments();
			shipments.setOrder_id(k);
			shipments.setProducts(v);
			shipmentsList.add(shipments);
		});
		logger.debug("List of shipments object " + shipmentsList);

		logger.debug("saveShipmentsDetails() ended");

		shipmentsRepository.saveAll(shipmentsList);
	}

	/**
	 * This method return the response for tracking api
	 * 
	 * @param queryParam
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Map<String, String> getTrackingResponse(String queryParam) {
		logger.debug("getTrackingResponse() started");

		APIConfig apiConfig = null;
		Map<String, String> deserializeResponse = null;
		if (StringUtils.isEmpty(queryParam)) {
			throw new MissingQueryParameterException(
					"The tracking API request is missing query params" + queryParam);
		}
		try {
			jmsTemplateTracking.convertAndSend(jmsTemplateTracking.getDefaultDestinationName(), queryParam);

			Thread.sleep(200);

			apiConfig = apiConfigRepository.findById(2).orElseThrow(
					() -> new ResourceNotFoundException("In getTrackingResponse(), API with ID :" + 2 + " Not Found!"));
			if (apiConfig.getRequest_count() > 0) {

				ResponseEntity<String> response = getResponseAsString(Constant.TRACKING,
						StringUtils.chop(apiConfig.getQuery_param()));

				deserializeResponse = new ObjectMapper().readValue(response.getBody(),
						new TypeReference<Map<String, String>>() {
						});
				saveTrackingDetails(deserializeResponse);
				if (apiConfig.getRequest_count() == 5) {
					logger.info("End of cap for Tracking requests");

					updateRequestCountAndQueryParam(2);
				}
			}
		} catch (IOException | InterruptedException | JmsException e) {
			logger.error("Exception in getTrackingResponse() " + e.getLocalizedMessage());
		}
		logger.debug("getTrackingResponse() ended");

		return deserializeResponse;
	}

	/**
	 * Method to save the Tracking response object into DB
	 * 
	 * @param deserializeResponse
	 */
	private void saveTrackingDetails(Map<String, String> deserializeResponse) {
		logger.debug("saveTrackingDetails() started");

		List<Tracking> trackingList = new ArrayList<Tracking>();
		deserializeResponse.forEach((k, v) -> {
			Tracking tracking = new Tracking();
			tracking.setOrder_id(k);
			tracking.setOrder_status(v);
			trackingList.add(tracking);
		});
		logger.debug("List of Tracking objects " + trackingList);

		trackingRepository.saveAll(trackingList);
		logger.debug("saveTrackingDetails() ended");

	}

	/**
	 * This method return the response as string for an end point
	 * 
	 * @param apiPath, queryParam
	 * @return
	 */
	private ResponseEntity<String> getResponseAsString(String apiPath, String queryParams) {
		logger.debug("getResponseAsString() started");

		String url = Constant.HOSTNAME + Constant.THIRD_PARTY_PORT + Constant.FORWARD_SLASH + apiPath
				+ Constant.QUESTION_STRING + Constant.QUERY_PARAM_STRING + Constant.EQUALS_STRING + queryParams;

		logger.debug("getResponseAsString() ended");

		return restTemplateBuilder.build().exchange(url, HttpMethod.GET, null, String.class);
	}

	/**
	 * This method return the response for pricing api
	 * 
	 * @param queryParam
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws JMSException
	 */
	public Map<String, Float> getPricingResponse(String queryParam) {
		logger.debug("getPricingResponse() started");

		APIConfig apiConfig = null;
		Map<String, Float> deserializeResponse = null;
		if (StringUtils.isEmpty(queryParam)) {
			throw new MissingQueryParameterException(
					"The pricing API request is invalid" + queryParam);
		}
		try {
			jmsTemplatePricing.convertAndSend(jmsTemplatePricing.getDefaultDestinationName(), queryParam);

			Thread.sleep(200);
			apiConfig = apiConfigRepository.findById(3).orElseThrow(
					() -> new ResourceNotFoundException("In getPricingResponse(), API with ID :" + 3 + " Not Found!"));

			if (apiConfig.getRequest_count() > 0) {
				ResponseEntity<String> response = getResponseAsString(Constant.PRICING,
						StringUtils.chop(apiConfig.getQuery_param()));
				deserializeResponse = new ObjectMapper().readValue(response.getBody(),
						new TypeReference<Map<String, Float>>() {
						});

				savePricingDetails(deserializeResponse);
				if (apiConfig.getRequest_count() == 5) {
					logger.info("End of cap for Pricing requests");

					updateRequestCountAndQueryParam(3);
				}
			}
		} catch (IOException | InterruptedException | JmsException e) {
			logger.error("Exception in getTrackingResponse() " + e.getLocalizedMessage());
		}
		logger.debug("getPricingResponse() ended");

		return deserializeResponse;
	}

	/**
	 * Method to update the request count( completing the cap) and query param
	 * 
	 * @param id
	 */
	private void updateRequestCountAndQueryParam(Integer id) {
		logger.debug("updateRequestCountAndQueryParam() started");

		APIConfig apiConfig = apiConfigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				"In Method updateRequestCountAndQueryParam(), API details with id: " + id + " not found."));
		apiConfig.setRequest_count(0);
		apiConfig.setQuery_param("");
		apiConfig.setFirst_request_timestamp(null);
		apiConfigRepository.save(apiConfig);
		logger.debug("updateRequestCountAndQueryParam() ended");

	}

	/**
	 * Method to save pricing response objects to DB
	 * 
	 * @param deserializeResponse
	 */
	private void savePricingDetails(Map<String, Float> deserializeResponse) {
		logger.debug("savePricingDetails() started");

		List<Pricing> pricingList = new ArrayList<Pricing>();
		deserializeResponse.forEach((k, v) -> {
			Pricing pricing = new Pricing();
			pricing.setCountry_code(k);
			pricing.setPricing_value(v);
			pricingList.add(pricing);
		});
		logger.debug("List of pricing objects" + pricingList);

		pricingRepository.saveAll(pricingList);
		logger.debug("savePricingDetails() started");

	}

	/**
	 * This method configures converters in rest template builder
	 * 
	 * @param queryParam
	 * @return
	 */
	public void updatedResponseBuilder() {
		logger.debug("updatedResponseBuilder() started");
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(mediaTypes);
		messageConverters.add(converter);
		restTemplateBuilder.additionalMessageConverters(messageConverters);
		logger.debug("updatedResponseBuilder() started");

	}

}
