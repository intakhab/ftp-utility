package com.app.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class CommonUtil {
	private static final Logger logger = LogManager.getLogger("Common-Session");
	private CommonUtil() {
		
	}
	
	/**
	 * 
	 * @param fileName
	 * @param desDir
	 * @return
	 * @throws FileException
	 */
	public static String modifyFileName(String fileName, String desDir) throws FTPException {
		String extension = StringUtils.getFilenameExtension(fileName);
		String basename = FilenameUtils.getBaseName(fileName);
		final String currentFileName = desDir.concat(File.separator) + basename.concat("_") + System.currentTimeMillis()
				+ "." + extension;
		return currentFileName;

	}
	
	
	 /***
	    * 
	    * @param uploadFile
	    * @throws FTPException
	    */
	public static void createBackupFile(File uploadFile, String destDir) throws FTPException {
		
			File destLocalDir = new File(destDir);
			if (!destLocalDir.exists()) {
				destLocalDir.mkdirs();
				logger.info("Folder created for backup " + destLocalDir);
			}
			final String destFilePath = CommonUtil.modifyFileName(uploadFile.getName(), destDir);
			logger.info("File is moving to location " + destFilePath);
			try {
				// FileUtils.moveFileToDirectory(uploadFile, destLocalDir, true);
				FileUtils.moveFile(FileUtils.getFile(uploadFile), FileUtils.getFile(destFilePath));
			} catch (IOException e) {
				logger.error("Error at copying file in destination dir- " + destLocalDir);
			}
			logger.info("File moved to backup file " + destDir);
		}

}
