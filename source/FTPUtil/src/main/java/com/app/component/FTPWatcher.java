package com.app.component;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.util.FTPException;
import com.app.util.FTPScheduler;

@Component
public class FTPWatcher {
	private Logger logger = Logger.getLogger(FTPWatcher.class);
	@Autowired
	FTPScheduler ftpScheduler;

	public FTPWatcher() {
	}

	@Scheduled(fixedRateString = "${ftp.pollertime}")
	public void create() {
		final LocalDateTime start = LocalDateTime.now();
		logger.info("....................................................................");
		logger.info("::::::::::     Starting Pooling ... " + start+"  :::::::::");

		try {
			ftpScheduler.toInvoke();
		} catch (FTPException e) {
			logger.error("Error : "+ e.getMessage());
			//e.printStackTrace();
		}
		logger.info("::::::::::     Ending Pooling  ... " + start+"  :::::::::");
		logger.info("....................................................................");

	}

	public FTPScheduler getFtpScheduler() {
		return ftpScheduler;
	}

	public void setFtpScheduler(FTPScheduler ftpScheduler) {
		this.ftpScheduler = ftpScheduler;
	}

}
