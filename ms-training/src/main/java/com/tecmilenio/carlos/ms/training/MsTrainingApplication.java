package com.tecmilenio.carlos.ms.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTrainingApplication.class, args);
	}

}
