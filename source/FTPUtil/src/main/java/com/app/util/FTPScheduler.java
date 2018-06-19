package com.app.util;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.config.PropertiesConfig;

@Configuration
public class FTPScheduler {

	protected Logger logger = Logger.getLogger(FTPScheduler.class);

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	private FTPSession ftpSession;

	public void toInvoke() throws FTPException {

		if (ftpSession.onInit()) {
			if ("true".equals(propertiesConfig.ftpDownloaderEnable)) {
				try {
					createDownloadSession();
				} catch (FTPException e) {
					throw new FTPException("File Downloading problem :  " + e.getMessage());
				}
			}if("true".equals(propertiesConfig.ftpUploaderEnable)) {
				try {
					createUploadSession();
				} catch (FTPException e) {
					throw new FTPException("File Uploading problem:  " + e.getMessage());
				}
			}
		}
	}

	private void createUploadSession() throws FTPException {
		scanLocalDirToUpload();

	}

	private void createDownloadSession() throws FTPException {
		FTPFile[] ftpFiles = ftpSession.listFiles(propertiesConfig.ftpDownloadRemoteDir, propertiesConfig.filePattern);
		if (ftpFiles != null && ftpFiles.length > 0) {
			logger.info("Remote got " + ftpFiles.length + " to check");
			for (FTPFile ftpFile : ftpFiles) {
				String fileName = ftpFile.getName();
				String localFilePath = propertiesConfig.ftpDownloadLocalDir + File.separator + fileName;
				ftpSession.downloadFile(fileName, localFilePath);
			}
		} else {
			logger.info("File is not available at FTP server...");

		}

	}

	public void scanLocalDirToUpload() throws FTPException {
		logger.info("scan local folder " + propertiesConfig.ftpUploaderLocalDir);
		File localDirFile = new File(propertiesConfig.ftpUploaderLocalDir);
		if (localDirFile.exists()) {
			logger.info("local dir existed, scanning");
			File[] localFiles = null;
			if (propertiesConfig.filePattern.isEmpty()) {
				localFiles = localDirFile.listFiles();
			} else {
				localFiles = localDirFile.listFiles(new WildcardFilenameFilter(propertiesConfig.filePattern));
			}
			for (File file : localFiles) {
				logger.info(
						"Uploading file  ( " + file.getName() + " )  to FTP Direcotry " + propertiesConfig.ftpUploaderRemoteDir);
				ftpSession.uploadFile(file, propertiesConfig.ftpUploaderRemoteDir);
				logger.info("File uploaded successfully...");
			}
		} else {
			logger.info("local dir not existed, creating");
			localDirFile.mkdirs();
		}
	}
	

}
