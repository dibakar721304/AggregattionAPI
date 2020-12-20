package com.dibakar.aggregation.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(as = TNTaggregation.class)
public class TNTaggregation {

	@JsonProperty("")
	private Map<String, List<String>> shipments;
	@JsonProperty("")
	private Map<String, Float> pricing;
	@JsonProperty("")
	private Map<String, String> tracking;

	public Map<String, List<String>> getShipments() {
		return shipments;
	}

	public void setShipments(Map<String, List<String>> shipments) {
		this.shipments = shipments;
	}

	public Map<String, Float> getPricing() {
		return pricing;
	}

	public void setPricing(Map<String, Float> pricing) {
		this.pricing = pricing;
	}

	public Map<String, String> getTracking() {
		return tracking;
	}

	public void setTracking(Map<String, String> tracking) {
		this.tracking = tracking;
	}

}