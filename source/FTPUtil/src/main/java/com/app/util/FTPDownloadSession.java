package com.app.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.config.PropertiesConfig;

/**
 * 
 * @author intakhabalam.s
 *
 */
@Configuration
public class FTPDownloadSession {
	private Logger logger = LogManager.getLogger("Download-Session");
	
    @Autowired
	PropertiesConfig propertiesConfig;
	private FTPClient ftpClient;

   
	/**
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPException 
	 * @throws NumberFormatException 
	 */
	public boolean onInit() throws FTPException {
		//buildConfiguration();
		
		if ("false".equals(propertiesConfig.proxyDownloadEnable)) {
			
			logger.info("Trying to connect ftp client without proxy initilazing FTPClient");
			
			ftpClient = new FTPClient();
		} else {
			logger.info("Trying to connect ftp client with proxy initilazing FTPHTTPClient");
			
			ftpClient = new FTPHTTPClient(propertiesConfig.proxyDownloadServer,
					Integer.parseInt(propertiesConfig.proxyDownloadPort), propertiesConfig.proxyDownloadUsername,
					propertiesConfig.proxyDownloadPassword);
		}
		if("true".equals(propertiesConfig.ftpEnableCommandlineLog)) {
		  ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
    
		
		return connect();
	}
	
	/**
	 * Connect and login to the server.
	 * 
	 * @throws FTPException
	 */
	public boolean connect() throws FTPException {
		boolean logged=false;
		try {

			logger.info("Connecting ftp server address ( " + propertiesConfig.ftpDownloadServer + ")  with port ( "
					+ propertiesConfig.ftpDownloadPort + " )");

			ftpClient.connect(propertiesConfig.ftpDownloadServer, Integer.valueOf(propertiesConfig.ftpDownloadPort));

			int replyCode = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			logged = ftpClient.login(propertiesConfig.ftpDownloadUser, propertiesConfig.ftpDownloadPass);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				throw new FTPException("Could not login to the server.");
			}
			ftpClient.enterLocalPassiveMode();
			//ftpClient.enterRemotePassiveMode();

			logger.info("Connected to ftp client.");

		} catch (IOException ex) {
			throw new FTPException("I/O error: " + ex.getMessage());
		}
		return logged;
	}
	
	  @PostConstruct
      private void buildConfiguration() {
		  
		if ("true".equals(propertiesConfig.ftpDownloaderEnable)) {

		  logger.info("-------------------------------------------------------------------------------------");
		    logger.info("Configuration information  for FTP Downloader");
		    logger.info(":: FTP Downloader Enable :: "+propertiesConfig.ftpDownloaderEnable);
		    logger.info(":: Local Directory for Uploader :: "+propertiesConfig.ftpDownloadLocalDir);
		    logger.info(":: Remote Directory for Uploader :: "+propertiesConfig.ftpDownloadRemoteDir);
		    logger.info(":: FTP Server :: "+propertiesConfig.ftpDownloadServer);
		    logger.info(":: FTP Port :: "+propertiesConfig.ftpDownloadPort);
		    logger.info(":: FTP User :: "+propertiesConfig.ftpDownloadUser);
		    logger.info(":: FTP Pass :: "+propertiesConfig.ftpDownloadPass);
		    logger.info(":: File Pattern :: "+propertiesConfig.fileDownloadPattern);
		    logger.info(":: Keep Remote File :: "+propertiesConfig.keepDownloadRemoteFile);
		    int time=Integer.parseInt(propertiesConfig.ftpDownloadPollerTime)/1000;
		    logger.info(":: Poller Time :: "+time +"  (In Second)");
		    logger.info(":: Proxy enable Command Line Log :: "+propertiesConfig.ftpEnableCommandlineLog);
		    logger.info(":: Proxy enable :: "+propertiesConfig.proxyDownloadEnable);
		    if("true".equals(propertiesConfig.proxyUploadEnable)) {
	        	   logger.info(":: Proxy Server :: "+propertiesConfig.proxyDownloadServer);
	        	   logger.info(":: Proxy Port :: "+propertiesConfig.proxyDownloadPort);
	        	   logger.info(":: Proxy user :: "+propertiesConfig.proxyDownloadUsername);
	        	   logger.info(":: Proxy pass :: "+propertiesConfig.proxyDownloadPassword);
	           }
		  logger.info("-------------------------------------------------------------------------------------");
		}

      }
	/***
	 * 
	 * @param dirPath
	 * @param filePattern
	 * @return
	 * @throws IOException
	 * @throws FTPException 
	 */
	public FTPFile[] listFiles(String dirPath, String filePattern) throws  FTPException{
		try {
			reConnect();
			logger.info("Scaning remote folder ( "+dirPath +" )");
			boolean success =ftpClient.changeWorkingDirectory(dirPath);
			if (!success) {
				throw new FTPException("Could not change working directory to " 
						+ dirPath + ". The directory may not exist.");
				
			}
			if(filePattern.isEmpty()) {
				 logger.info("files size : "+ftpClient.listFiles().length);
				 return ftpClient.listFiles();// all files
				
			}else {
				 logger.info("finding file with pattern : "+filePattern);
				 logger.info("files size : "+ftpClient.listFiles().length);
			     return ftpClient.listFiles(filePattern);
			}
		} catch (IOException e) {
			throw new FTPException("Error to finding list of files. "+e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param localFilePath
	 * @throws IOException
	 * @throws FTPException 
	 */
	public void downloadFile(String fileName, String localFilePath) throws FTPException {
		logger.info("Before download check connection availability.");
		try {
			reConnect();
			boolean success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				throw new FTPException("Could not set binary file type.");
			}
			ftpClient.setFileTransferMode(1);
			File localFile = new File(localFilePath);
			localFile.createNewFile();

			try (FileOutputStream localFileOS = new FileOutputStream(localFile)) {
				ftpClient.retrieveFile(fileName, localFileOS);
				logger.info("File downloaded successfully to path " + localFile);
				// If you want to backup file from remote. put condition "true"
				if ("false".equals(propertiesConfig.keepDownloadRemoteFile)) {
					CommonUtil.createBackupFile(new File(fileName), propertiesConfig.ftpUploadRemoteDirBackup);
					// FileUtils.deleteQuietly(new File(fileName));
				}
				// localFileOS.close();
			}

		} catch (Exception ex) {
			throw new FTPException("Error occurs during uploading file " + ex.getMessage());
		} finally {
			disconnect();
		}

	}
	
	
	
	/**
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPException 
	 */
	public void reConnect() throws FTPException{
		if(ftpClient.isConnected()){
			logger.info("Connection already available");
		} else {
			logger.info("Connection lost, trying to re-connect");
			connect();
		}
	}
	/**
	 * Log out and disconnect from the server
	 */
	@PreDestroy
	public void disconnect() throws FTPException {
		
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException ex) {
				throw new FTPException("Error disconnect from the server: "	+ ex.getMessage());
			}
		}
		logger.info("Login disconnected...");

	}
	public void onDestroy(){}



	public FTPClient getFtpClient() {
		return ftpClient;
	}


	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}


			
}
