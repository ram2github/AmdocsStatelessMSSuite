package com.amdocs.domain.billing.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amdocs.domain.billing.config.BillingConfig;
import com.amdocs.domain.billing.resources.Bill;


@Controller
@RestController
@RequestMapping(value="/billing")
public class BillingManagementService {
	
private Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	@Autowired
	private BillingConfig billingConfig;
	
	private static int counter=0;
		
	
	/***
	 * This method is invoked when http://localhost:8080/quote/ is invoked
	 * @param restTemplate - restTemplate is a singleton bean autowired and injected
	 * @return Bill - Response containing Billing details
	 */
	@RequestMapping(value="/",method = RequestMethod.GET)
	public @ResponseBody Bill getBillDetails(){
	
 	   logger.info("request recieved - getBillDetails");
	
	   Bill customerBill = new Bill();
	   
	   customerBill.setBillDetails(String.format("\nPort >>> %s<<<< In Domain %s,Bill %s_%d was retrieved successfully\n",
			   										billingConfig.getLocalServerPort(),
			   										billingConfig.getDomain(),
			   										billingConfig.getId_prefix(), 
			   										counter++));
	   
	   return customerBill;
		
	}
  
	
}