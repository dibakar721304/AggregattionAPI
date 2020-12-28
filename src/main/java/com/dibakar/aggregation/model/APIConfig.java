package com.dibakar.aggregation.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator(name="CONFIG_SEQ", sequenceName="config_sequence")
@Table(name = "api_config")
public class APIConfig {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CONFIG_SEQ")
	@Column(name = "api_config_id") 
	private int api_config_id;

	@Override
	public String toString() {
		return "APIConfig [api_config_id=" + api_config_id + ", api_name=" + api_name + ", request_count="
				+ request_count + ", query_param=" + query_param + ", first_request_timestamp="
				+ first_request_timestamp + "]";
	}

	@Column(name = "api_name")
	private String api_name;
	

	@Column(name = "request_count")
	private int request_count;
	
	@Column(name = "query_param")
	private String query_param;
	
	@Column(name = "first_request_timestamp")
	private LocalDateTime first_request_timestamp;
	
	public int getApi_config_id() {
		return api_config_id;
	}

	public void setApi_config_id(int api_config_id) { 
		this.api_config_id = api_config_id;
	}

	
	public String getQuery_param() {
		return query_param;
	}

	public void setQuery_param(String query_param) {
		this.query_param = query_param;
	}

	public String getApi_name() {
		return api_name;
	}

	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}

	public int getRequest_count() {
		return request_count;
	}

	public void setRequest_count(int request_count) {
		this.request_count = request_count;
	}

	public LocalDateTime getFirst_request_timestamp() {
		return first_request_timestamp;
	}

	public void setFirst_request_timestamp(LocalDateTime first_request_timestamp) {
		this.first_request_timestamp = first_request_timestamp;
	}


}
