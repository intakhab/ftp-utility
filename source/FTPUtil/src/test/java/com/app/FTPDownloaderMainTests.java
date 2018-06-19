package com.app;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.util.FTPException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FTPDownloaderMainTests {
	
	/**
	 * Prepare test for remote FTP
	 * @throws Exception
	 */

	@Test
	public void contextLoads() {
	}
	
	/**
	 * Test a ls for a mocked FTP server
	 * 
	 * @throws IOException
	 * @throws FTPException
	 * @throws ParseException
	 */
	@Test
	public void ftpTest() throws IOException, FTPException, ParseException {
		// Must return two files
		SpringApplication.run(FTPDownloaderMain.class);

	}

	

	
}
