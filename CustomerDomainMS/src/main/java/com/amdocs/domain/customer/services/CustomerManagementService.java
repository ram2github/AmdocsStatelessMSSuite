package com.amdocs.domain.customer.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.amdocs.domain.customer.config.CustomerConfig;
import com.amdocs.domain.customer.resources.CustomerOrder;
import com.amdocs.domain.ordering.resources.OrderBill;


@Controller
@RestController
@RequestMapping(value="/customer")
public class CustomerManagementService {
	
	private Logger logger = LoggerFactory.getLogger(getClass().getName());
	

	@Autowired
	private RestTemplateBuilder restTemplateBuilder; // this is injected by spring boot framework itself
	
	@Autowired
	private CustomerConfig customerConfig;
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	private static int counter=0;
	
	/**
	 * Create a singleton restTemplate to be used by the application
	 * No one calls this 
	 * @param restTemplateBuilder - is injected by Spring using the @Autowired logic
	 * @return RestTemplate
	 */
	@Bean 
	public RestTemplate restTemplate(){
		logger.info("Check Config server Load: Domain : "+ customerConfig.getDomain() + " ID_PREFIX :" + customerConfig.getId_prefix());
		return restTemplateBuilder.build();
	}
	
	@Autowired
	RestTemplate restTemplate;
	
	
	/* You have to register RestTemplate as a bean so that the interceptors will get injected. 
	 * If you create a RestTemplate instance with a new keyword then the zipkin tracing instrumentation WILL NOT work.
	*/
	/***
	 * This method is invoked when http://localhost:8080/quote/ is invoked
	 * @param restTemplate - restTemplate is a singleton bean autowired and injected
	 * @return CustomerOrder - Response containing Customer , Order and Billing details
	 */
	//@RequestMapping(value="/",method = RequestMethod.GET)
	@GetMapping
	public @ResponseBody CustomerOrder getCustomerOrders(){
	
	   logger.info("request recieved - getCustomerOrders");
	   CustomerOrder customerOrder = new CustomerOrder();//restTemplate.getForObject(qsConfig.getUrl(), Quote.class);
	   customerOrder.setCustomerDetails(String.format("\nPort >>> %s<<<< In Domain %s,Customer %s_%d was retrieved successfully\n",
			   							    customerConfig.getLocalServerPort(),
			   								customerConfig.getDomain(),
			   								customerConfig.getId_prefix(), 
			   								counter++));
	   
	   List<ServiceInstance> gatewayServers = serviceInstancesByApplicationName("AMDOCS_GATEWAY_SERVER"); // pickup from Config Server
	   if(gatewayServers != null && gatewayServers.size()>0)
	   {
		   // call the gateway server to loadbalance the call to Ordering MS
		   
		   // gte the first gateway server
		   ServiceInstance gatewayServer = gatewayServers.get(0);
		   
		   // construct the new URI for restTeplate
		   String baseUrl = gatewayServer.getUri().toString();
		   baseUrl = baseUrl + "/order/";
		
		   
		   logger.info("URL for Order MS :" + baseUrl);
		   
		   OrderBill orderAndBillDetails = restTemplate.getForObject(baseUrl, OrderBill.class);
		   customerOrder.setOrderDetails(orderAndBillDetails.getOrderDetails());
		   customerOrder.setBillDetails(orderAndBillDetails.getBillDetails());

	   }
	   
	   return customerOrder;
		
	}

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
	
}