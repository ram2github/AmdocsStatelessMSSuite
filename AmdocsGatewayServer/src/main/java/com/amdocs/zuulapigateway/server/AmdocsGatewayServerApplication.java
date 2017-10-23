package com.amdocs.zuulapigateway.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class AmdocsGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmdocsGatewayServerApplication.class, args);
	}
	
	@Bean
	  public LoadbalacingRouteFilter simpleFilter() {
	    return new LoadbalacingRouteFilter();
	  }

	/*private static final Map<String,String> servletPath2ServiceName = new HashMap<String,String>();
	
	@Bean
	public Map<String,String> serviceNamesForDiscoveryLookup(){
		
		servletPath2ServiceName.put("customer", "Customer_Management_MS");
		servletPath2ServiceName.put("order", "ORDER_MANAGEMENT_MS");
		servletPath2ServiceName.put("billing", "BILLING_MANAGEMENT_MS");
		
		System.out.println("#################################################### service discovery using Eureka");
		return servletPath2ServiceName;
	}*/

}
