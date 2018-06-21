package com.app;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Main {

	public static void main(String[] args) {

		try {
			
			FileUtils.moveFileToDirectory(new File("c:/data2/file3.txt"), new File("c:/data2/backup"),true);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
