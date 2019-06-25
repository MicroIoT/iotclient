package top.microiot.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import top.microiot.api.HttpSessionProperties;
import top.microiot.api.client.HttpClientSession;

@Configuration
public class BikeHttpClientSessionConfig {
	@Bean(initMethod = "start", name="bikeHttpClientSession")
    @Scope("prototype")
	public HttpClientSession httpClientSession() {
		return new HttpClientSession(httpSessionProperties());
	}
	
	@Bean("bikeHttpProperties")
    @ConfigurationProperties(prefix = "bike.connect")
    public HttpSessionProperties httpSessionProperties(){
	    return new HttpSessionProperties();
    }
}
