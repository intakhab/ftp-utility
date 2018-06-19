package com.app.util;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.FilenameUtils;

public class WildcardFilenameFilter implements FilenameFilter {

	
     private String filePattern;
	
	public WildcardFilenameFilter(){}

	public WildcardFilenameFilter(String filePattern) {
		this.filePattern = filePattern;
	}

	@Override
	public boolean accept(File dir, String fileName) {
		return FilenameUtils.wildcardMatch(fileName, filePattern);//filepattern=*.xml.gz
		
	}
}
