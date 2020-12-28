package com.dibakar.aggregation.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.dibakar.aggregation.util.StringListConverter;

@Entity
@SequenceGenerator(name="SHIPMENTS_SEQ", sequenceName="shipments_sequence")

@Table(name = "shipments")
public class Shipments {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SHIPMENTS_SEQ")
	@Column(name = "shipment_id")
	private int shipment_id;

	@Column(name = "order_id")
	private String order_id;
	
	@Convert(converter = StringListConverter.class)
	@Column(name = "products")
	private List<String> products;

	public int getShipment_id() {
		return shipment_id;
	}

	public void setShipment_id(int shipment_id) {
		this.shipment_id = shipment_id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

}
