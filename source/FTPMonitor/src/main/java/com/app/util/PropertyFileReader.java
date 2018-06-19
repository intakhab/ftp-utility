package com.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileReader {
	public static Properties prop = new Properties();

	static {

		InputStream input = null;

		try {

			String filename = "application.properties";
			input = PropertyFileReader.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				
			}
			// load a properties file from class path, inside static method
			prop.load(input);

		

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
