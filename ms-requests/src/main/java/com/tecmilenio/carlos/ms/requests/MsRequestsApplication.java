package com.tecmilenio.carlos.ms.requests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsRequestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRequestsApplication.class, args);
	}

}
