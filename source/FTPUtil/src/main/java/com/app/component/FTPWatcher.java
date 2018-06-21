package com.app.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.config.PropertiesConfig;
import com.app.util.FTPDownloadScheduler;
import com.app.util.FTPUploadScheduler;

@Component
public class FTPWatcher {
	private Logger logger = LogManager.getLogger("FTP-Watcher");
	@Autowired
	FTPDownloadScheduler ftpDownloadScheduler;
	
	@Autowired
	FTPUploadScheduler ftpUploadScheduler;
	@Autowired
	PropertiesConfig propertiesConfig;
	
	public FTPWatcher() {
	}

	@Scheduled(fixedRateString = "${ftp.download.pollertime}")
	public void ftpDownloadScheduler() {
		
		if ("true".equals(propertiesConfig.ftpDownloaderEnable)) {

			logger.info("Started Downloader.......");
			final long startTime = System.currentTimeMillis();
			logger.info("Starting Pooling Time ... " + startTime);
			try {
				ftpDownloadScheduler.toInvoke();
			} catch (Exception e) {
				logger.error("Exception at ftpDownloadScheduler() :" + e.fillInStackTrace());
			}
			final long endTime = System.currentTimeMillis();
			final long totalTimeTaken = (endTime - startTime);
			logger.info("Ending Pooling Time ... " + endTime);
			logger.info("Downloader completed Polling  in " + totalTimeTaken + " (Seconds)");
			logger.info("Finshed Downloader.......");
		} else {
			logger.info("Downloader session disabled.");
		}

	}
	
	@Scheduled(fixedRateString = "${ftp.upload.pollertime}",initialDelayString ="${ftp.download.pollertime}")
	public void ftpUploadScheduler() {
		if ("true".equals(propertiesConfig.ftpUploaderEnable)) {

			final long startTime = System.currentTimeMillis();
			logger.info("Started uploader....... Pooling Time ... " + startTime);
			try {
				ftpUploadScheduler.toInvoke();
			} catch (Exception e) {
				logger.error("Exception  at ftpUploadScheduler () " + e.getMessage());
			}
			final long endTime = System.currentTimeMillis();
			final long totalTimeTaken = (endTime - startTime);
			logger.info("Finishing Uploader....... Pooling Time ... " + endTime);
			logger.info("Uploader completed Polling  in " + totalTimeTaken + " (Seconds)");
		}else {
			logger.info("Uploader session disabled.");

		}

	}

	
	

}
