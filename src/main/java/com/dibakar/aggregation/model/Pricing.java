package com.dibakar.aggregation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(as = Pricing.class)
public class Pricing {

	@JsonProperty("NL")
	private float nl;

	@JsonProperty("CN")
	private float cn;
	public float getNl() {
		return nl;
	}

	public void setNl(float nl) {
		this.nl = nl;
	}

	public float getCn() {
		return cn;
	}

	public void setCn(float cn) {
		this.cn = cn;
	}

}
