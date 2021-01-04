package com.dibakar.test.integration;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dibakar.aggregation.AggregationApplication;
import com.dibakar.aggregation.controller.AggregationController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AggregationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AggregationAPIintegrationTest {
	@Autowired
	private AggregationController aggregationController;
	 @Test
	    public void contextLoads() {
	        Assertions
	          .assertThat(aggregationController)
	          .isNotNull();

	    }

	@Test
	public void testForAggregationAPI() throws Exception {
		HttpUriRequest request = new HttpGet("http://localhost:8081/aggregation?pricing=FR&track=109347263&shipments=109347263");

		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		Assertions.assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);

	}
	@Test
	public void testForShipmentsAPI() throws Exception {
		HttpUriRequest request = new HttpGet("http://localhost:8081/shipments?q=123456891");

		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		Assertions.assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);

	}
	@Test
	public void testForPricingAPI() throws Exception {
		HttpUriRequest request = new HttpGet("http://localhost:8081/pricing?q=US");

		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		Assertions.assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);

	}
	@Test
	public void testForTrckingAPI() throws Exception {
		HttpUriRequest request = new HttpGet("http://localhost:8081/track?q=109347263");

		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		Assertions.assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);

	}


}
