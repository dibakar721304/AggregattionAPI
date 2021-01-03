package com.dibakar.aggregation.controller;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dibakar.aggregation.exception.ResourceNotFoundException;
import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.repository.APIConfigRepository;
import com.dibakar.aggregation.services.AggregationServices;

@RunWith(SpringRunner.class)
@WebMvcTest(AggregationController.class)
public class AggregationControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockBean
	JmsTemplate jmsTemplateShipments;

	@MockBean
	APIConfigRepository apiConfigRepository;

	@MockBean
	private AggregationServices aggregationServices;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void init() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	@After
	public void setDown() throws Exception {
		mvc = null;

	}

	@Test
	public void testAggregationAPI() throws Exception {
		TNTaggregation aggregateResponse = new TNTaggregation();
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("pricing", "FR");
		queryParams.put("track", "109347263");
		queryParams.put("shipments", "109347263");

		when(aggregationServices.getAggregateResponse(queryParams)).thenReturn(aggregateResponse);

		mvc.perform(get("/aggregation?pricing=FR&track=109347263&shipments=109347263")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testAggregationAPIExceptionCase() throws Exception {
		when(aggregationServices.getAggregateResponse(null)).thenReturn(null);
		mvc.perform(get("/aggregation?pricing=FR&track=109347263&shipments=109347263")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
	}

	@Test
	public void testShipmentAPI() throws Exception {
		Map<String, List<String>> shipmentResponse = new HashMap<String, List<String>>();
		ArrayList<String> listOfProducts = new ArrayList<String>();
		listOfProducts.add("box");
		shipmentResponse.put("123456891", listOfProducts);
		when(aggregationServices.getShipmentResponse("123456891")).thenReturn(shipmentResponse);

		mvc.perform(get("/shipments?q=123456891").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testShipmentAPIExceptionCase() throws Exception {
		when(aggregationServices.getShipmentResponse(null)).thenReturn(null);
		mvc.perform(get("/shipments?q=123456891").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
	}

	@Test
	public void testTrackingAPI() throws Exception {
		Map<String, String> trackingResponse = new HashMap<String, String>();
		trackingResponse.put("109347263", "COLLECTING");
		when(aggregationServices.getTrackingResponse("109347263")).thenReturn(trackingResponse);

		mvc.perform(get("/track?q=109347263").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasValue("COLLECTING")));
	}

	@Test
	public void testTrackingAPIExceptionCase() throws Exception {
		when(aggregationServices.getTrackingResponse(null)).thenReturn(null);
		mvc.perform(get("/track?q=109347263").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
	}

	@Test
	public void testPricingAPI() throws Exception {
		Map<String, Float> pricingResponse = new HashMap<String, Float>();
		pricingResponse.put("US", 11.10612F);
		when(aggregationServices.getPricingResponse("US")).thenReturn(pricingResponse);

		mvc.perform(get("/pricing?q=US").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasValue(11.10612)));
	}

	@Test
	public void testPricingAPIExceptionCase() throws Exception {
		when(aggregationServices.getPricingResponse(null)).thenReturn(null);
		mvc.perform(get("/pricing?q=US").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
	}

}
