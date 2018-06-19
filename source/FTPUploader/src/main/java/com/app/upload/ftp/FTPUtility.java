package com.app.upload.ftp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.app.FileUploadFTPMain;

/**
 * A utility class that provides functionality for uploading files to a FTP
 * server.
 * @author intakhabalam.s
 */
public class FTPUtility {

	private String host;
	private int port;
	private String username;
	private String password;

	private FTPClient ftpClient = new FTPClient();
	private int replyCode;

	private OutputStream outputStream;
	private Logger logger = Logger.getLogger(FTPUtility.class);

	
	public FTPUtility(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.username = user;
		this.password = pass;
		config();
	}
	
    void config() {
    	logger.info("***************************************");
    	logger.info("Host: "+host);
    	logger.info("Port: "+port);
    	logger.info("Username: "+username);
    	logger.info("Password: "+password);
    	logger.info("***************************************");
    	
    }

	/**
	 * Connect and login to the server.
	 * 
	 * @throws FTPException
	 */
	public void connect() throws FTPException {
		try {
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
		    	logger.error("FTP serve refused connection.");
				throw new FTPException("FTP serve refused connection.");
			}

			boolean logged = ftpClient.login(username, password);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
		    	logger.error("Could not login to the server.");
				throw new FTPException("Could not login to the server.");
			}

			ftpClient.enterLocalPassiveMode();

		} catch (IOException ex) {
	    	logger.error("I/O error: " + ex.getMessage());
			throw new FTPException("I/O error: " + ex.getMessage());
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
		try {
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
			
		} catch (IOException ex) {
			logger.error("Error uploading file: " + ex.getMessage());
			throw new FTPException("Error uploading file: " + ex.getMessage());
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
	 * Log out and disconnect from the server
	 */
	public void disconnect() throws FTPException {
		if (ftpClient.isConnected()) {
			try {
				if (!ftpClient.logout()) {
					throw new FTPException("Could not log out from the server");
				}
				ftpClient.disconnect();
			} catch (IOException ex) {
				throw new FTPException("Error disconnect from the server: "
						+ ex.getMessage());
			}
		}
	}
}