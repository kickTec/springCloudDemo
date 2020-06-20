package org.cxytiandi.conf.springboot.demo;

import org.cxytiandi.conf.client.init.ConfInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
	
	//可以使用Bean的创建方式来初始化配置客户端
	@Bean
	public ConfInit confInit() {
		return new ConfInit();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
