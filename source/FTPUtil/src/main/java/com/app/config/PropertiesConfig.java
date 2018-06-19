package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
// @ConfigurationProperties(prefix = "ftp")
public class PropertiesConfig {
	@Value("${ftp.server}")
	 public String ftpServer;

	@Value("${ftp.port}")
	public String ftpPort;

	@Value("${ftp.user}")
	public String ftpUsername;

	@Value("${ftp.pass}")
	public String ftpPassword;

	@Value("${ftp.proxy.server}")
	public String proxyServer;

	@Value("${ftp.proxy.port}")
	public String proxyPort;

	@Value("${ftp.proxy.user}")
	public String proxyUsername;

	@Value("${ftp.proxy.pass}")
	public String proxyPassword;

	@Value("${ftp.download.local.dir}")
	public String ftpDownloadLocalDir;
	
	@Value("${ftp.download.remote.dir}")
	public String ftpDownloadRemoteDir;
	
	
	@Value("${ftp.upload.local.dir}")
	public String ftpUploaderLocalDir;

	@Value("${ftp.upload.remote.dir}")
	public String ftpUploaderRemoteDir;
	
	@Value("${file.pattern}")
	public String filePattern;
	
	@Value("${ftp.proxy.enable}")
	public String proxyEnable;

	@Value("${ftp.keep.file}")
	public String keepRemoteFile;
	
	@Value("${ftp.pollertime}")
	public String ftpPollerTime;
	
	@Value("${ftp.enable.commandline.log}")
	public String ftpEnableCommandlineLog;
	
	@Value("${ftp.downloader.enable}")
	public String ftpDownloaderEnable;
	
	@Value("${ftp.uploader.enable}")
	public String ftpUploaderEnable;

	

	
	
	
}
