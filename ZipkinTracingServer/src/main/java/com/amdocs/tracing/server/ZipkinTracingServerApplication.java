package com.amdocs.tracing.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import zipkin.server.EnableZipkinServer;

@EnableDiscoveryClient
@EnableZipkinServer
@SpringBootApplication
public class ZipkinTracingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinTracingServerApplication.class, args);
	}
}
