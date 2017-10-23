package com.amdocs.domain.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker
public class OrderingDomainMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingDomainMsApplication.class, args);
	}
}
