package com.dangeacademy;

import com.dangeacademy.config.cloudflare.CloudflareProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CloudflareProperties.class)
public class ClassesManagementApplication {


	public static void main(String[] args) {


		System.setProperty(
				"aws.java.v1.disableDeprecationAnnouncement",
				"true"
		);

		SpringApplication.run(ClassesManagementApplication.class, args);
	}


}
