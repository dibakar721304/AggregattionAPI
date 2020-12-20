package com.dibakar.aggregation.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@SequenceGenerator(name="TRACKING_SEQ", sequenceName="tracking_sequence")

@Table(name = "tracking")
public class Tracking {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="TRACKING_SEQ")
	@Column(name = "tracking_id")
	private int tracking_id;

	@Column(name = "order_id")
	private String order_id;
	
	@Column(name = "order_status")
	private String order_status;

	public int getTracking_id() {
		return tracking_id;
	}

	public void setTracking_id(int tracking_id) {
		this.tracking_id = tracking_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	
}
