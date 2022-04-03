package com.ibm.security.appscan.bigbucks.api;


import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

import com.ibm.security.appscan.bigbucks.filter.ApiAuthFilter;

@ApplicationPath("api")
public class BigbucksAPI extends ResourceConfig {
	public BigbucksAPI(){
		packages("com.ibm.security.appscan.bigbucks.api");
		register(ApiAuthFilter.class);
	}
}
