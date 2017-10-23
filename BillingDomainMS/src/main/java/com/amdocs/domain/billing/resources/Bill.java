package com.amdocs.domain.billing.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bill {

	private String billDetails;

	public String getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}
	
}
