package com.amdocs.domain.ordering.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.amdocs.domain.billing.resources.Bill;
import com.amdocs.domain.ordering.config.OrderingConfig;
import com.amdocs.domain.ordering.resources.OrderBill;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@Controller
@RestController
@RequestMapping(value="/order")
@Service
public class OrderManagementService {
	
private Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder; // this is injected by spring boot framework itself
	
	@Autowired
	private OrderingConfig orderingConfig;
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	@Autowired
	RestTemplate restTemplate;
	
	private static int counter=0;
	
	/**
	 * Create a singleton restTemplate to be used by the application
	 * No one calls this 
	 * @param restTemplateBuilder - is injected by Spring using the @Autowired logic
	 * @return RestTemplate
	 */
	@Bean 
	public RestTemplate restTemplate(){
		logger.info("Check Config server Load: Domain : "+ orderingConfig.getDomain() + " ID_PREFIX :" + orderingConfig.getId_prefix());
		return restTemplateBuilder.build();
	}
	
	/***
	 * This method is invoked when http://localhost:8080/quote/ is invoked
	 * @param restTemplate - restTemplate is a singleton bean autowired and injected
	 * @return CustomerOrder - Response containing Order and Billing details
	 */
	@HystrixCommand(fallbackMethod = "handleBillingFailure")
	@RequestMapping(value="/",method = RequestMethod.GET)
	public @ResponseBody OrderBill getOrderDetails() {

		logger.info("request recieved - getOrderDetails");

		OrderBill orderBill = new OrderBill();// restTemplate.getForObject(qsConfig.getUrl(),
												// Quote.class);
		orderBill.setOrderDetails(
				String.format("\nPort >>> %s<<<< In Domain %s,Order %s_%d was retrieved successfully\n",
									orderingConfig.getLocalServerPort(), 
									orderingConfig.getDomain(), 
									orderingConfig.getId_prefix(),
									counter++));

		List<ServiceInstance> gatewayServers = serviceInstancesByApplicationName("AMDOCS_GATEWAY_SERVER"); // TODO : pickup form config server
		if (gatewayServers != null && gatewayServers.size() > 0) {
			// call the gateway server to loadbalance the call to Ordering MS

			// gte the first gateway server
			ServiceInstance gatewayServer = gatewayServers.get(0);

			// construct the new URI for restTeplate
			String baseUrl = gatewayServer.getUri().toString();
			baseUrl = baseUrl + "/billing/";

			logger.info("API Gateway URL for Billing MS :" + baseUrl);

			Bill billDetails = restTemplate.getForObject(baseUrl, Bill.class);
			orderBill.setBillDetails(billDetails.getBillDetails());
		}
		return orderBill;

	}

	public OrderBill handleBillingFailure() {
		OrderBill orderBill = new OrderBill();//restTemplate.getForObject(qsConfig.getUrl(), Quote.class);
		orderBill.setOrderDetails(String.format("\nPort >>> %s<<<< In Domain %s,Order %s_%d was retrieved successfully\n",
													orderingConfig.getLocalServerPort(), 
													orderingConfig.getDomain(),
			   										orderingConfig.getId_prefix(), 
			   										counter));
		return orderBill;
	 }
	
    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
	
}