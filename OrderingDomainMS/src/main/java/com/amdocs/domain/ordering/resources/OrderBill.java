package com.amdocs.domain.ordering.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBill {

	private String orderDetails;

	public String getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	private String billDetails;

	public String getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}
	
}
