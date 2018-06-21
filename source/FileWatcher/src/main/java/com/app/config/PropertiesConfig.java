package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertiesConfig {
	@Value("${polling.time}")
	public String pollingTime;
	
	@Value("${input.folder}")
	public String inputFolder;
	
	@Value("${output.folder}")
	public String outputFolder;
	
	@Value("${output.temp.folder}")
	public String outputTempFolder;
	
	@Value("${archive.folder}")
	public String archiveFolder;
	
	
	
	

}
