package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.app")
public class FTPDownloaderMain {

	public static void main(String[] args) {
		SpringApplication.run(FTPDownloaderMain.class, args);
    
	}
	
	
	
}
