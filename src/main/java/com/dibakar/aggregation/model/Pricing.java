package com.dibakar.aggregation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name="PRICING_SEQ", sequenceName="pricing_sequence")
@Table(name = "pricing")
public class Pricing {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PRICING_SEQ")
	@Column(name = "pricing_id")
	private int pricing_id;

	@Column(name = "country_code")
	private String country_code;
	
	@Column(name = "pricing_value")
	private float pricing_value;

	public int getPricing_id() {
		return pricing_id;
	}

	public void setPricing_id(int pricing_id) {
		this.pricing_id = pricing_id;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public float getPricing_value() {
		return pricing_value;
	}

	public void setPricing_value(float pricing_value) {
		this.pricing_value = pricing_value;
	}

	

}
