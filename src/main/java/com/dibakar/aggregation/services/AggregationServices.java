package com.dibakar.aggregation.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.model.Pricing;
import com.dibakar.aggregation.model.Shipments;
import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.model.Tracking;
import com.dibakar.aggregation.repository.PricingRepository;
import com.dibakar.aggregation.repository.ShipmentsRepository;
import com.dibakar.aggregation.repository.TrackingRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class holds the business logic and interation with different layers
 * 
 * @author 31612
 *
 */
@Service
@Transactional
public class AggregationServices {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	private final PricingRepository pricingRepository;

	private final ShipmentsRepository shipmentsRepository;
	private final TrackingRepository trackingRepository;

	public AggregationServices(PricingRepository pricingRepository, ShipmentsRepository shipmentsRepository,
			TrackingRepository trackingRepository) {
		super();
		this.pricingRepository = pricingRepository;
		this.shipmentsRepository = shipmentsRepository;
		this.trackingRepository = trackingRepository;

	}

	/**
	 * This method populates the response from different services and bind it to
	 * TNTaggregation object
	 * 
	 * @param queryParam
	 * @return
	 */
	public TNTaggregation getAggregateResponse(Map<String, String> queryParams) {

		TNTaggregation tntAggregation = new TNTaggregation();
		updatedResponseBuilder();
		Map<String, List<String>> shipments = null;
		Map<String, String> tracking = null;
		Map<String, Float> pricing = null;
		try {
			shipments = getSipmentResponse(queryParams.get(Constant.SHIPMENTS));
			tracking = getTrackingResponse(queryParams.get(Constant.TRACKING));
			pricing = getPricingResponse(queryParams.get(Constant.PRICING));
		} catch (IOException ioException) {
		}

		tntAggregation.setPricing(pricing);
		tntAggregation.setTracking(tracking);
		tntAggregation.setShipments(shipments);

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
	public Map<String, List<String>> getSipmentResponse(String queryParam)
			throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<String> response = getResponseAsString(Constant.SHIPMENTS, queryParam);

		Map<String, List<String>> deserializeResponse = null;

		deserializeResponse = new ObjectMapper().readValue(response.getBody(),
				new TypeReference<Map<String, List<String>>>() {
				});
		saveShipmentsDetails(deserializeResponse);
		return deserializeResponse;
	}

	private void saveShipmentsDetails(Map<String, List<String>> deserializeResponse) {
		List<Shipments> shipmentsList = new ArrayList<Shipments>();
		deserializeResponse.forEach((k, v) -> {
			Shipments shipments = new Shipments();
			shipments.setOrder_id(k);
			shipments.setProducts(v);
			shipmentsList.add(shipments);
		});
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
	public Map<String, String> getTrackingResponse(String queryParam)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, String> deserializeResponse = null;
		ResponseEntity<String> response = getResponseAsString(Constant.TRACKING, queryParam);

		deserializeResponse = new ObjectMapper().readValue(response.getBody(),
				new TypeReference<Map<String, String>>() {
				});
		saveTrackingDetails(deserializeResponse);
		return deserializeResponse;
	}

	private void saveTrackingDetails(Map<String, String> deserializeResponse) {
		List<Tracking> trackingList = new ArrayList<Tracking>();
		deserializeResponse.forEach((k, v) -> {
			Tracking tracking = new Tracking();
			tracking.setOrder_id(k);
			tracking.setOrder_status(v);
			trackingList.add(tracking);
		});
		trackingRepository.saveAll(trackingList);
	}

	/**
	 * This method return the response as string for an end point
	 * 
	 * @param apiPath, queryParam
	 * @return
	 */
	private ResponseEntity<String> getResponseAsString(String apiPath, String queryParams) {
		String url = Constant.HOSTNAME + Constant.THIRD_PARTY_PORT + Constant.FORWARD_SLASH + apiPath
				+ Constant.QUESTION_STRING + Constant.QUERY_PARAM_STRING + Constant.EQUALS_STRING + queryParams;
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
	 */
	public Map<String, Float> getPricingResponse(String queryParam)
			throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<String> response = getResponseAsString(Constant.PRICING, queryParam);
		Map<String, Float> deserializeResponse = null;

		deserializeResponse = new ObjectMapper().readValue(response.getBody(), new TypeReference<Map<String, Float>>() {
		});
		savePricingDetails(deserializeResponse);
		return deserializeResponse;
	}

	private void savePricingDetails(Map<String, Float> deserializeResponse) {
		List<Pricing> pricingList = new ArrayList<Pricing>();
		deserializeResponse.forEach((k, v) -> {
			Pricing pricing = new Pricing();
			pricing.setCountry_code(k);
			pricing.setPricing_value(v);
			pricingList.add(pricing);
		});
		pricingRepository.saveAll(pricingList);
	}

	/**
	 * This method configures converters in rest template builder
	 * 
	 * @param queryParam
	 * @return
	 */
	private void updatedResponseBuilder() {

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(mediaTypes);
		messageConverters.add(converter);
		restTemplateBuilder.additionalMessageConverters(messageConverters);
	}

}
