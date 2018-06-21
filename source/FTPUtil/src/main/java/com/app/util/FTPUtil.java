package com.app.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * @author intakhabalam.s
 *
 */
public class FTPUtil {
	protected Logger logger = LogManager.getLogger("FTPUtil");

	public static void downloadDirectory(FTPClient ftpClient, String parentDir, String currentDir, String saveDir)
			throws IOException {

		String dirToList = parentDir;
	    if (!currentDir.equals("")) {
	        dirToList += File.separator + currentDir;
	    }
	    FTPFile[] subFiles = ftpClient.listFiles(dirToList);
	    if (subFiles != null && subFiles.length > 0) {
	        for (FTPFile aFile : subFiles) {
	            String currentFileName = aFile.getName();
	            if (currentFileName.equals(".") || currentFileName.equals("..")) {
	                // skip parent directory and the directory itself
	                continue;
	            }
	            String filePath = parentDir +File.separator+ currentDir + File.separator
	                    + currentFileName;
	            if (currentDir.equals("")) {
	                filePath = parentDir + File.separator + currentFileName;
	            }
	 
	            String newDirPath = saveDir + parentDir + File.separator
	                    + currentDir + File.separator + currentFileName;
	            if (currentDir.equals("")) {
	                newDirPath = saveDir + parentDir + File.separator
	                          + currentFileName;
	            }
	 
	            if (aFile.isDirectory()) {
	                // create the directory in saveDir
	                File newDir = new File(newDirPath);
	                boolean created = newDir.mkdirs();
	                if (created) {
	                    System.out.println("Created the directory: " + newDirPath);
	                } else {
	                   System.out.println("Could  not create the directory: " + newDirPath);
	                }
	 
	                // download the sub directory
	                downloadDirectory(ftpClient, dirToList, currentFileName,
	                        saveDir);
	            } else {
	                // download the file
	                boolean success = downloadSingleFile(ftpClient, filePath,
	                        newDirPath);
	                if (success) {
	                    System.out.println("Downloaded the file: " + filePath);
	                } else {
	                    System.out.println("Could not download the file: "
	                            + filePath);
	                }
	            }
	        }
	    }

	}

	/**
	 *  * Download a single file from the FTP server  * @param ftpClient an instance
	 * of org.apache.commons.net.ftp.FTPClient class.  * @param remoteFilePath path
	 * of the file on the server  * @param savePath path of directory where the file
	 * will be stored  * @return true if the file was downloaded successfully, false
	 * otherwise  * @throws IOException if any network or IO error occurred.  
	 */
	public static boolean downloadSingleFile(FTPClient ftpClient, String remoteFilePath, String savePath)
			throws IOException {
		File downloadFile = new File(savePath);
		File parentDir = downloadFile.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}

		// code to download a file...
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return ftpClient.retrieveFile(remoteFilePath, outputStream);
		} catch (IOException ex) {
			throw ex;
		}

	}
	
	
}
