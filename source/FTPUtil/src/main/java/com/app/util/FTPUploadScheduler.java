package com.app.util;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.config.PropertiesConfig;

@Configuration
public class FTPUploadScheduler {

	protected Logger logger = LogManager.getLogger("FTP-Upload-Scheduler");

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	private FTPUploadSession ftpSession;

	public void toInvoke() throws FTPException {

			if (ftpSession.onInit()) {

				try {
					createUploadSession();
				} catch (FTPException e) {
					throw new FTPException("File Uploading problem at FTP server:  " + e.getMessage());
				}
			} else {
				logger.info(
						"Upload session is disabled. for enable change in properties file. [ftp.uploader.enable=true]");
			}
	}

	private void createUploadSession() throws FTPException {
		scanLocalDirToUpload();

	}

	public void scanLocalDirToUpload() throws FTPException {
		logger.info("Scan local folder  ( " + propertiesConfig.ftpUploadLocalDir +" )");
		File localDirFile = new File(propertiesConfig.ftpUploadLocalDir);
		if (localDirFile.exists()) {
			logger.info("local dir existed, scanning");
			File[] localFiles = null;
			if (propertiesConfig.fileUploadPattern.isEmpty()) {
				localFiles = localDirFile.listFiles();
			} else {
				localFiles = localDirFile.listFiles(new FTPFilenameFilter(propertiesConfig.fileUploadPattern));
			}
			
			if (localFiles != null && localFiles.length > 0) {
				for (File file : localFiles) {
					if (file.isFile()) {
						logger.info("Uploading file  ( " + file.getName() + " )  to FTP Direcotry "
								+ propertiesConfig.ftpUploadRemoteDir);
						ftpSession.uploadFile(file, propertiesConfig.ftpUploadRemoteDir);
					}
				}
			} else {
				logger.info("File is not available at local folder to upload.");

			}
			ftpSession.disconnect();
		} else {
			logger.info("local dir not existed, creating ");
			localDirFile.mkdirs();
		}
	}

}
