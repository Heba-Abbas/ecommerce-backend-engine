package com.ecommerce.backend_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BackendEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendEngineApplication.class, args);
	}

}
