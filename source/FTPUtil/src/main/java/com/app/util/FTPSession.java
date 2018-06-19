package com.app.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.app.config.PropertiesConfig;

/**
 * 
 * @author intakhabalam.s
 *
 */
@Configuration
public class FTPSession {
	private Logger logger = Logger.getLogger(FTPSession.class.getName());
	
    @Autowired
	PropertiesConfig propertiesConfig;
	private FTPClient ftpClient;
	private OutputStream outputStream;

   
	
	/**
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPException 
	 * @throws NumberFormatException 
	 */
	public boolean onInit() throws FTPException {
		//buildConfiguration();
		int _proxyPort = Integer.parseInt(propertiesConfig.proxyPort);
		if ("false".equals(propertiesConfig.proxyEnable)) {
			logger.info("Trying to connect ftp client without proxy initilazing FTPClient");
			ftpClient = new FTPClient();
		} else {
			logger.info("Trying to connect ftp client with proxy initilazing FTPHTTPClient");
			ftpClient = new FTPHTTPClient(propertiesConfig.proxyServer, _proxyPort, propertiesConfig.proxyUsername,
					propertiesConfig.proxyPassword);
		}
		if("true".equals(propertiesConfig.ftpEnableCommandlineLog)) {
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

			logger.info("Connecting ftp server address ( " + propertiesConfig.ftpServer + ")  with port ( "
					+ propertiesConfig.ftpPort + " )");

			ftpClient.connect(propertiesConfig.ftpServer, Integer.valueOf(propertiesConfig.ftpPort));

			int replyCode = ftpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			logged = ftpClient.login(propertiesConfig.ftpUsername, propertiesConfig.ftpPassword);
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
		    logger.info("**********Configuration information Start ********************");
		    logger.info(":: FTP Uploader Enable :: "+propertiesConfig.ftpUploaderEnable);
		    if("true".equals(propertiesConfig.ftpUploaderEnable)) {
		    	logger.info(":: Local Directory for Uploader :: "+propertiesConfig.ftpUploaderLocalDir);
		    	logger.info(":: Remote Directory for Uploader :: "+propertiesConfig.ftpUploaderRemoteDir);
		    }
		    logger.info(":: FTP Downloader Enable :: "+propertiesConfig.ftpDownloaderEnable);
		    if("true".equals(propertiesConfig.ftpDownloaderEnable)) {
		    	logger.info(":: Local Directory for Downloader :: "+propertiesConfig.ftpDownloadLocalDir);
		    	logger.info(":: Remote Directory for Downloader :: "+propertiesConfig.ftpDownloadRemoteDir);
		    }

		    logger.info(":: FTP Server :: "+propertiesConfig.ftpServer);
		    logger.info(":: FTP Port :: "+propertiesConfig.ftpPort);
		    logger.info(":: FTP User :: "+propertiesConfig.ftpUsername);
		    logger.info(":: FTP Pass :: "+propertiesConfig.ftpPassword);
		    logger.info(":: File Pattern :: "+propertiesConfig.filePattern);
		    logger.info(":: Keep Remote File :: "+propertiesConfig.keepRemoteFile);
		    int time=Integer.parseInt(propertiesConfig.ftpPollerTime)/1000;
		    logger.info(":: Poller Time :: "+time +"  (In Second)");
		    logger.info(":: Proxy enable :: "+propertiesConfig.proxyEnable);
		    logger.info(":: Proxy enable Command Line Log :: "+propertiesConfig.ftpEnableCommandlineLog);

		    
           if("true".equals(propertiesConfig.proxyEnable)) {
        	   logger.info(":: Proxy Server :: "+propertiesConfig.proxyServer);
        	   logger.info(":: Proxy Port :: "+propertiesConfig.proxyPort);
        	   logger.info(":: Proxy user :: "+propertiesConfig.proxyUsername);
        	   logger.info(":: Proxy pass :: "+propertiesConfig.proxyPassword);
           }

		  logger.info("**********Configuration information End ********************");
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
			logger.info("scan remote folder "+dirPath);
			boolean success =ftpClient.changeWorkingDirectory(dirPath);
			if (!success) {
				throw new FTPException("Could not change working directory to " 
						+ dirPath + ". The directory may not exist.");
				
			}
			if(filePattern.isEmpty()) {
				 logger.info("Loaded files size :: "+ftpClient.listFiles().length);
				 return ftpClient.listFiles();// all files
				
			}else {
				 logger.info("Loaded files with pattern size :: "+ftpClient.listFiles().length);
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
	public void downloadFile(String fileName, String localFilePath) throws  FTPException {
		logger.info("Before download check connection availability.");
		try {
			reConnect();
			boolean success=ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				throw new FTPException("Could not set binary file type.");
			}
			ftpClient.setFileTransferMode(1);
			File localFile = new File(localFilePath);
			localFile.createNewFile();
			
			try (FileOutputStream localFileOS = new FileOutputStream(localFile)) {
				ftpClient.retrieveFile(fileName, localFileOS);
				logger.info("File downloaded successfully at path "+localFile);
				//If you want to delete file from remote..
				if ("false".equals(propertiesConfig.keepRemoteFile)) {
					FileUtils.deleteQuietly(new File(fileName));
					logger.info("Deleting file from remote path "+fileName);
				}
			 // localFileOS.close();
			}
			
		} catch (SocketException ex) {
			throw new FTPException("Error uploading file 1 :: " + ex.getMessage());
		 } catch (IOException ex) {
			throw new FTPException("Error uploading file 2 :: " + ex.getMessage());
		}finally {
			disconnect();
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
		logger.info("Before upload check connection availability.");
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
			
			outputStream = ftpClient.storeFileStream(uploadFile.getName());
			logger.info("File uploaded successfully.");

		} catch (IOException ex) {
			logger.error("Error uploading file: " + ex.getMessage());
			throw new FTPException("Error uploading file: " + ex.getMessage());
		}finally {
			try {
				outputStream.close();
				finish();
			} catch (IOException e) {
				logger.error("I/O Error : " + e);

			}
			disconnect();
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
	 * Complete the upload operation.
	 */
	public void finish() throws IOException {
		outputStream.close();
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
				if (!ftpClient.logout()) {
					throw new FTPException("Could not log out from the server");
				}
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
