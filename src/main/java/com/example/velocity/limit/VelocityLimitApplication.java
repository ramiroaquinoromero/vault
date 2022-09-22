package com.example.velocity.limit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class VelocityLimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(VelocityLimitApplication.class, args);
	}

}
