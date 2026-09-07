package com.priyanshu.iims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntelligentIncidentManagementApplication {

	public static void main(String[] args) {

	    var context = SpringApplication.run(
	            IntelligentIncidentManagementApplication.class,
	            args
	    );

	    var env = context.getEnvironment();

	    System.out.println("======================================");
	    System.out.println("MONGO DB  = " + env.getProperty("spring.mongodb.uri"));
	    System.out.println("MONGO DB  = " + env.getProperty("spring.mongodb.database"));
	    System.out.println("======================================");
	}
}
