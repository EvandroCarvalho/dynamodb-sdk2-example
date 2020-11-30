package com.example.dynamodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class DynamodbJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamodbJavaApplication.class, args);
	}
}
