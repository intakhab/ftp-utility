package com.app.util;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.config.PropertiesConfig;

@Configuration
public class FTPDownloadScheduler {

	protected Logger logger = LogManager.getLogger("FTP-DU-Scheduler");

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	private FTPDownloadSession ftpSession;

	public void toInvoke() throws FTPException {
			if (ftpSession.onInit()) {
				try {
					createDownloadSession();
				} catch (FTPException e) {
					throw new FTPException("Downloading problem  from FTP :  " + e.getMessage());
				}
			} else {
				logger.info(
						"Download session is disabled. for enable change in properties file. [ftp.download.enable=true]");
			}
		}

	private void createDownloadSession() throws FTPException {
		FTPFile[] ftpFiles = ftpSession.listFiles(propertiesConfig.ftpDownloadRemoteDir,
				propertiesConfig.fileDownloadPattern);
		if (ftpFiles != null && ftpFiles.length > 0) {
			logger.info("Remote got " + ftpFiles.length + " to check");
			for (FTPFile ftpFile : ftpFiles) {
				String fileName = ftpFile.getName();
				String localFilePath = propertiesConfig.ftpDownloadLocalDir + File.separator + fileName;
				ftpSession.downloadFile(fileName, localFilePath);
			}
		} else {
			logger.info("File is not available at FTP server to download.");

		}

	}

}
