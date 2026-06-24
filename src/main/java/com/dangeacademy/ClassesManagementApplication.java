package com.dangeacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassesManagementApplication {


	public static void main(String[] args) {


		System.setProperty(
				"aws.java.v1.disableDeprecationAnnouncement",
				"true"
		);

		SpringApplication.run(ClassesManagementApplication.class, args);
	}


}
