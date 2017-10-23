package com.amdocs.zuulapigateway.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class LoadbalacingRouteFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(LoadbalacingRouteFilter.class);

	@Autowired
	private LoadBalancerClient loadBalancer;
	
	
	@Autowired
	private Map<String,String> serviceNamesForDiscoveryLookup;
		
	/*
	
	Zuul has four standard filter types:
	-------------------------------------
	pre -filters are executed before the request is routed,
	route - filters can handle the actual routing of the request,
	post - filters are executed after the request has been routed, and
	error - filters execute if an error occurs in the course of handling the request.
	
	------------------------------------------------------------------------------------
	
	Filter classes implement four methods:
    --------------------------------------
	filterType() returns a String that stands for the type of the filter---in this case, pre, or it could be route for a routing filter.
	filterOrder() gives the order in which this filter will be executed, relative to other filters.
	shouldFilter() contains the logic that determines when to execute this filter (this particular filter will always be executed).
	run() contains the functionality of the filter.
		
	*/
	
	@Override
	public String filterType() {
		return "route";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		boolean shouldFilterFlag = false;

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		log.info(String.format("shouldFilter - %s request to %s was originally routed to %s", 
									request.getMethod(),
									request.getRequestURI(), 
									ctx.getRouteHost()));
	
		String[] context = request.getServletPath().split("/");
		for(String S: context) 
			log.info("context:" + S);
		
		log.info("ServiceNames : " + serviceNamesForDiscoveryLookup.toString());
		
		if(context !=null)
		{
			if(context.length>1)
			   shouldFilterFlag = serviceNamesForDiscoveryLookup.containsKey(context[1]);
		}

		log.info("shouldFilter returning :" + shouldFilterFlag);
		return shouldFilterFlag;
	}

	@Override
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String serviceName = serviceNamesForDiscoveryLookup.get(
											request.getServletPath()
													.split("/")
													[1]);

		try {
			ServiceInstance serviceInstance = loadBalancer.choose(serviceName);
			String baseUrl = serviceInstance.getUri().toString();
			baseUrl = baseUrl + request.getRequestURI();
			log.info("base Url : " + baseUrl);
			ctx.setRouteHost(new URL(baseUrl));
			log.info("ServerListInfo :"  + serviceInstance.getUri());
			log.info(String.format("run - %s request to %s was now loadbalanced to %s", 
								request.getMethod(),
								request.getRequestURI(), 
								ctx.getRouteHost()));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}