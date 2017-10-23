package com.amdocs.domain.ordering.config;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RefreshScope
@Configuration
public class OrderingConfig{

	@NotNull
	@NotEmpty
	@Value("${OrderingConfig.domain}")
	private String domain;

	@Value("${OrderingConfig.id_prefix}")
	@NotEmpty
	private String id_prefix;

	private String localServerPort;
	
	@Autowired
	public Environment environment;
	
	public String getLocalServerPort() {
		return environment.getProperty("local.server.port");
	}

	public void setLocalServerPort(String localServerPort) {
		this.localServerPort = localServerPort;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId_prefix() {
		return id_prefix;
	}

	public void setId_prefix(String id_prefix) {
		this.id_prefix = id_prefix;
	}

	@Override
	public String toString() {
		return " Domain : +" + domain +
				" prefix : " + id_prefix;
	}
}