package com.tecmilenio.ms.employees.ms_employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsEmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEmployeesApplication.class, args);
	}

}
