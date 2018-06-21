package com.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileWatcher {
	private Logger logger = LogManager.getLogger("FileWatcher");
	@Autowired
	FileSession fileUtil;

	
	
	@Scheduled(fixedRateString = "${polling.time}")
	public void create() {
		final long startTime=System.currentTimeMillis();
		logger.info("Starting Pooling Time ... " + startTime);
		try {
			fileUtil.moveFile();
		} catch (Exception e) {
			logger.error("Exception "+e.fillInStackTrace());
		}
		final long endTime=System.currentTimeMillis();
		final long totalTimeTaken=(endTime-startTime);
		logger.info("Ending Pooling Time ... " +endTime);
		logger.info("Total time taken to complete Polling ... " +totalTimeTaken +" (Seconds)");


	}
   
	

	
	
}
