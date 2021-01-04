package com.dibakar.test.aggregation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;

import com.dibakar.aggregation.model.TNTaggregation;
import com.dibakar.aggregation.repository.APIConfigRepository;
import com.dibakar.aggregation.repository.PricingRepository;
import com.dibakar.aggregation.repository.ShipmentsRepository;
import com.dibakar.aggregation.repository.TrackingRepository;
import com.dibakar.aggregation.services.AggregationServices;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = AggregationServices.class)
public class AggregationServicesTest {
	@MockBean
	JmsTemplate jmsTemplateShipments;

	private AggregationServices aggregationServices;
	@MockBean
	private RestTemplateBuilder restTemplateBuilder;
	@MockBean
	private APIConfigRepository apiConfigRepository;
	@MockBean
	private PricingRepository pricingRepository;
	@MockBean
	private ShipmentsRepository shipmentsRepository;
	@MockBean
	private TrackingRepository trackingRepository;
	Map<String, String> queryParams = new HashMap<String, String>();
	Map<String, List<String>> shipments = null;
	Map<String, String> tracking = null;
	Map<String, Float> pricing = null;
	ArrayList<String> listOfProducts = null;
	TNTaggregation tntAggregation = null;

	@Before
	public void setUp() {
		aggregationServices = Mockito.spy(AggregationServices.class);
		tntAggregation = Mockito.spy(TNTaggregation.class);
		queryParams = new HashMap<String, String>();
		queryParams.put("pricing", "FR");
		queryParams.put("track", "109347263");
		queryParams.put("shipments", "109347263");
		shipments = new HashMap<String, List<String>>();
		listOfProducts = new ArrayList<String>();
		listOfProducts.add("box");
		shipments.put("123456891", listOfProducts);
		tracking = new HashMap<String, String>();
		tracking.put("109347263", "COLLECTING");
		pricing = new HashMap<String, Float>();
		pricing.put("US", 11.10612F);
	}

	@After
	public void setDown() {
		aggregationServices = null;
		queryParams = null;
		listOfProducts = null;
		shipments = null;
		tracking = null;
		pricing = null;

	}

	@Test
	public void testAggregateResponseWhenOtherApiReturnNullResponse() {

		Mockito.doNothing().when(aggregationServices).updatedResponseBuilder();
		Mockito.doReturn(null).when(aggregationServices).getShipmentResponse(queryParams.get("shipments"));
		Mockito.doReturn(null).when(aggregationServices).getTrackingResponse(queryParams.get("track"));
		Mockito.doReturn(null).when(aggregationServices).getPricingResponse(queryParams.get("pricing"));
		TNTaggregation tntAggregationSpyObj = aggregationServices.getAggregateResponse(queryParams);
		assertNull(tntAggregationSpyObj.getPricing());
		assertNull(tntAggregationSpyObj.getTracking());
		assertNull(tntAggregationSpyObj.getShipments());

	}

	@Test
	public void testAggregateResponse() {

		Mockito.doNothing().when(aggregationServices).updatedResponseBuilder();
		Mockito.doReturn(shipments).when(aggregationServices).getShipmentResponse(queryParams.get("shipments"));
		Mockito.doReturn(tracking).when(aggregationServices).getTrackingResponse(queryParams.get("track"));
		Mockito.doReturn(pricing).when(aggregationServices).getPricingResponse(queryParams.get("pricing"));
		TNTaggregation tntAggregationSpyObj = aggregationServices.getAggregateResponse(queryParams);
		assertEquals(pricing, tntAggregationSpyObj.getPricing());
		assertEquals(shipments, tntAggregationSpyObj.getShipments());
		assertEquals(tracking, tntAggregationSpyObj.getTracking());

	}
}
