package com.app.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class FTPUploadSession {
	private Logger logger = LogManager.getLogger("Upload-Session");
	
    @Autowired
	PropertiesConfig propertiesConfig;
	private FTPClient ftpClient;
	private OutputStream outputStream;
	private static final int BUFFER_SIZE = 4096;
	boolean done=false;


   
	/**
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPException
	 * @throws NumberFormatException
	 */
	public boolean onInit() throws FTPException {
		// buildConfiguration();
		int _proxyPort = Integer.parseInt(propertiesConfig.proxyUploadPort);
		if ("false".equals(propertiesConfig.proxyUploadEnable)) {
			logger.info("Trying to connect ftp client without proxy initilazing FTPClient");
			ftpClient = new FTPClient();
		} else {
			logger.info("Trying to connect ftp client with proxy initilazing FTPHTTPClient");
			ftpClient = new FTPHTTPClient(propertiesConfig.proxyUploadServer, _proxyPort,
					propertiesConfig.proxyUploadUsername, propertiesConfig.proxyUploadPassword);
		}
		if ("true".equals(propertiesConfig.ftpEnableCommandlineLog)) {
			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		}
		if (connect()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Connect and login to the server.
	 * 
	 * @throws FTPException
	 */
	public boolean connect() throws FTPException {
		boolean logged=false;
		try {

			logger.info("Connecting ftp server address ( " + propertiesConfig.ftpUploadServer + ")  with port ( "
					+ propertiesConfig.ftpUploadPort + " )");

			ftpClient.connect(propertiesConfig.ftpUploadServer, Integer.valueOf(propertiesConfig.ftpUploadPort));

			int replyCode = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			logged = ftpClient.login(propertiesConfig.ftpUploadUser, propertiesConfig.ftpUploadPass);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				throw new FTPException("Could not login to the server.");
			}
			ftpClient.enterLocalPassiveMode();

			logger.info("Connected to ftp client.");

		} catch (IOException ex) {
			throw new FTPException("I/O error: " + ex.getMessage());
		}
		return logged;
	}
	
	@PostConstruct
	private void buildConfiguration() {
		if ("true".equals(propertiesConfig.ftpUploaderEnable)) {

			logger.info("-------------------------------------------------------------------------------------");
			logger.info("Configuration information for FTP Uploader");
			logger.info(":: FTP Uploader Enable :: " + propertiesConfig.ftpUploaderEnable);
			logger.info(":: Local Directory for Uploader :: " + propertiesConfig.ftpUploadLocalDir);
			logger.info(":: Remote Directory for Uploader :: " + propertiesConfig.ftpUploadRemoteDir);
			logger.info(":: FTP Server :: " + propertiesConfig.ftpUploadServer);
			logger.info(":: FTP Port :: " + propertiesConfig.ftpUploadPort);
			logger.info(":: FTP User :: " + propertiesConfig.ftpUploadUser);
			logger.info(":: FTP Pass :: " + propertiesConfig.ftpUploadPass);
			logger.info(":: File Pattern :: " + propertiesConfig.fileUploadPattern);
			int time = Integer.parseInt(propertiesConfig.ftpUploadPollerTime) / 1000;
			logger.info(":: Poller Time :: " + time + "  (In Second)");
			logger.info(":: Proxy enable Command Line Log :: " + propertiesConfig.ftpEnableCommandlineLog);
			logger.info(":: Proxy enable :: " + propertiesConfig.proxyUploadEnable);
			if ("true".equals(propertiesConfig.proxyUploadEnable)) {
				logger.info(":: Proxy Server :: " + propertiesConfig.proxyUploadServer);
				logger.info(":: Proxy Port :: " + propertiesConfig.proxyUploadPort);
				logger.info(":: Proxy user :: " + propertiesConfig.proxyUploadUsername);
				logger.info(":: Proxy pass :: " + propertiesConfig.proxyUploadPassword);
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
			logger.info("Scanning remote folder ( "+dirPath +" )");
			boolean success =ftpClient.changeWorkingDirectory(dirPath);
			if (!success) {
				throw new FTPException("Could not change working directory to " 
						+ dirPath + ". The directory may not exist.");
				
			}
			if(filePattern.isEmpty()) {
				 logger.info("files size :  "+ftpClient.listFiles().length);
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
	 * Start uploading a file to the server
	 * @param uploadFile the file to be uploaded
	 * @param destDir destination directory on the server 
	 * where the file is stored
	 * @throws FTPException if client-server communication error occurred
	 */
	public void uploadFile(File uploadFile, String destDir) throws FTPException {
		done=false;//reset done 
		try {
			reConnect();
			boolean success = ftpClient.changeWorkingDirectory(destDir);
			if (!success) {
		    	logger.error("Could not change working directory to "
						+ destDir + ". The directory may not exist.");
				throw new FTPException("Could not change working directory to "
						+ destDir + ". The directory may not exist.");
				
			}
			
			success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);			
			if (!success) {
				logger.error("Could not set binary file type.");
				throw new FTPException("Could not set binary file type.");
			}
			try(InputStream inputStream = new FileInputStream(uploadFile)){
			//outputStream = ftpClient.storeFileStream(uploadFile.getName());
			//doInBackground(uploadFile.getName());
			final String remotePath=destDir+File.separator+uploadFile.getName();
			System.out.println("Saving to remote dir :"+remotePath);	
			 done=ftpClient.storeFile(remotePath,inputStream);
             if (done) {
                System.out.println("The file is uploaded successfully.");

             }
			}
		} catch (Exception ex) {
			throw new FTPException("Error uploading file: " + ex.getMessage());
		}finally {
			
			if (done) {
				try {
					CommonUtil.createBackupFile(uploadFile, propertiesConfig.ftpUploadLocalDirBackup);
				} catch (Exception e) {

				}
			}
		}
	}

	
  
	/**
	 * Write an array of bytes to the output stream.
	 */
	public void writeFileBytes(byte[] bytes, int offset, int length)
			throws IOException {
		outputStream.write(bytes, offset, length);
	}
	
	
	/**
	 * Executed in background thread
	 */	
	public void doInBackground(String uploadFile) throws Exception {
		try (FileInputStream inputStream = new FileInputStream(uploadFile);) {

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;
			long fileSize = uploadFile.length();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				writeFileBytes(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);
				// setProgress(percentCompleted);
				System.out.println("The file " + percentCompleted + " percentage uploadeding...");
			}

			finish();
		} catch (Exception ex) {
			throw new FTPException("Error during upload file ." + ex.getMessage());
		} finally {
			disconnect();
		}

	}

	/**
	 * Complete the upload operation.
	 */
	public void finish() throws IOException {
		if(outputStream!=null) {
		  outputStream.close();
		}
		ftpClient.completePendingCommand();
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
