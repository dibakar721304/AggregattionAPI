package com.dibakar.aggregation.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dibakar.aggregation.constant.Constant;
import com.dibakar.aggregation.model.Pricing;
import com.dibakar.aggregation.model.TNTaggregation;
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

	/**
	 * This method populates the response from different services and bind it to
	 * TNTaggregation object
	 * 
	 * @param queryParam
	 * @return
	 */
	public TNTaggregation getAggregateResponse(Map<String, String> queryParam) {

		TNTaggregation tntAggregation = new TNTaggregation();
		updatedResponseBuilder();
		Map<String, List<String>> shipments = getSipmentResponse(queryParam.get(Constant.SHIPMENTS));
		Map<String, String> tracking = getTrackingResponse(queryParam.get(Constant.TRACKING));
		Pricing pricing = getPricingResponse(queryParam.get(Constant.PRICING));

		tntAggregation.setShipments(shipments);
		tntAggregation.setTracking(tracking);

		tntAggregation.setPricing(pricing);
		return tntAggregation;
	}
/**
 * This method return the response for shipments api
 * @param queryParam
 * @return
 */
	private Map<String, List<String>> getSipmentResponse(String queryParam) {
		ResponseEntity<String> response = getResponseAsString(Constant.SHIPMENTS, queryParam);

		Map<String, List<String>> deserializeResponse = null;

		try {
			deserializeResponse = new ObjectMapper().readValue(response.getBody(),
					new TypeReference<Map<String, List<String>>>() {
					});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deserializeResponse;
	}
	/**
	 * This method return the response for tracking api
	 * @param queryParam
	 * @return
	 */
	private Map<String, String> getTrackingResponse(String queryParam) {

		Map<String, String> deserializeResponse = null;
		ResponseEntity<String> response = getResponseAsString(Constant.TRACKING, queryParam);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			deserializeResponse = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deserializeResponse;
	}
	/**
	 * This method return the response as string for an end point
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
	 * @param queryParam
	 * @return
	 */
	private Pricing getPricingResponse(String queryParam) {
		String url = Constant.HOSTNAME + Constant.THIRD_PARTY_PORT + Constant.FORWARD_SLASH + Constant.PRICING
				+ Constant.QUESTION_STRING + Constant.QUERY_PARAM_STRING + Constant.EQUALS_STRING + queryParam;
		ResponseEntity<Pricing> result = restTemplateBuilder.build().exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<Pricing>() {
				});
		return result.getBody();
	}
	/**
	 * This method configures converters in rest template builder
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
