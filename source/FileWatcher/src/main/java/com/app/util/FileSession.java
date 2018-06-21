package com.app.util;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.app.config.PropertiesConfig;

@Configuration
public class FileSession {
	private Logger logger = LogManager.getLogger("FileSession");

	@Autowired
	PropertiesConfig propertiesConfig;
	
	
	 @PostConstruct
     private void buildConfiguration() {
		    logger.info("**********Configuration information Start ********************");
		    logger.info("Polling Time : "+propertiesConfig.pollingTime);
		    logger.info("Polling Dir(Local Dir) : "+propertiesConfig.inputFolder);
		    logger.info("Destination Dir  : "+propertiesConfig.inputFolder);
		    logger.info("**********Configuration information End ********************");
     }
	 
	 /**
	  * 
	  * @throws FileException
	  */
	public void moveFile() throws FileException {
		try {
        
			String inputFolder = propertiesConfig.inputFolder;
			String outPutFolder=propertiesConfig.outputFolder;
			File dir = new File(inputFolder);
			logger.info("Scanning working directory "+inputFolder);
			File[] files = dir.listFiles();
			if (files.length == 0) {
				logger.info("The directory is empty.");
			} else {
				
				logger.info("The directory size "+files.length);

				for (File aFile : files) {
					logger.info("File name :"+aFile.getName());
					final String destFilePath=modifyFileName(aFile.getName(), outPutFolder);
					FileUtils.moveFile(FileUtils.getFile(aFile),
							FileUtils.getFile(destFilePath));
					logger.info("File is moving to location "+destFilePath);
				}
			}
			
		} catch (FileException e) {
			throw new FileException("FileException : "+e.getMessage());
		} catch (IOException e) {
			throw new FileException("I/O Exception : "+e.getMessage());

		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param desDir
	 * @return
	 * @throws FileException
	 */
	public static String modifyFileName(String fileName, String desDir) throws FileException {
		String extension = StringUtils.getFilenameExtension(fileName);
		String basename = FilenameUtils.getBaseName(fileName);
		final String currentFileName = desDir.concat(File.separator) + basename.concat("_") + System.currentTimeMillis()
				+ "." + extension;
		return currentFileName;

	}
}
