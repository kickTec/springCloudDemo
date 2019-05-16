package com.kenick;

import com.kenick.filter.AccessFilter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import sun.java2d.pipe.hw.AccelDeviceEventListener;

@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApiGatewayApplication.class).web(true).run(args);
	}

	@Bean
	public AccessFilter accessFilter(){
	    return new AccessFilter();
    }
}
