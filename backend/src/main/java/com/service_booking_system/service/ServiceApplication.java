// /backend/src/main/java/com/service_booking_system/service/ServiceApplication.java

package com.service_booking_system.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.service_booking_system")
public class ServiceApplication {

	public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().filename(".env").load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(ServiceApplication.class, args);
	}
}
