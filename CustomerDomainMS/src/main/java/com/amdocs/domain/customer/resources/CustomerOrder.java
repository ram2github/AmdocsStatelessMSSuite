package com.amdocs.domain.customer.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOrder {

	private String customerDetails;
	private String orderDetails;
	private String billDetails;
	
	public String getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails(String customerDetails) {
		this.customerDetails = customerDetails;
	}
	public String getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}
	
}
