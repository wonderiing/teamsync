package com.tecmilenio.carlos.ms.attendance.ms_attendancce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsAttendancceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAttendancceApplication.class, args);
	}

}
