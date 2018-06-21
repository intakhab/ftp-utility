package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
// @ConfigurationProperties(prefix = "ftp")
public class PropertiesConfig {
	@Value("${ftp.download.server}")
	public String ftpDownloadServer;

	@Value("${ftp.download.port}")
	public String ftpDownloadPort;

	@Value("${ftp.download.user}")
	public String ftpDownloadUser;

	@Value("${ftp.download.pass}")
	public String ftpDownloadPass;

	@Value("${ftp.download.local.dir}")
	public String ftpDownloadLocalDir;

	@Value("${ftp.download.remote.dir}")
	public String ftpDownloadRemoteDir;

	@Value("${ftp.download.file.pattern}")
	public String fileDownloadPattern;

	@Value("${ftp.download.remote.file.keep}")
	public String keepDownloadRemoteFile;

	@Value("${ftp.download.local.file.archive}")
	public String keepDownloadLocalFileArchive;

	@Value("${ftp.download.pollertime}")
	public String ftpDownloadPollerTime;
	
	@Value("${ftp.download.proxy.server}")
	public String proxyDownloadServer;

	@Value("${ftp.download.proxy.port}")
	public String proxyDownloadPort;

	@Value("${ftp.download.proxy.user}")
	public String proxyDownloadUsername;

	@Value("${ftp.download.proxy.pass}")
	public String proxyDownloadPassword;

	/////////////

	@Value("${ftp.upload.server}")
	public String ftpUploadServer;

	@Value("${ftp.upload.port}")
	public String ftpUploadPort;

	@Value("${ftp.upload.user}")
	public String ftpUploadUser;

	@Value("${ftp.upload.pass}")
	public String ftpUploadPass;

	@Value("${ftp.upload.local.dir}")
	public String ftpUploadLocalDir;

	@Value("${ftp.upload.remote.dir}")
	public String ftpUploadRemoteDir;

	@Value("${ftp.upload.file.pattern}")
	public String fileUploadPattern;

	@Value("${ftp.upload.pollertime}")
	public String ftpUploadPollerTime;
	
	@Value("${ftp.upload.proxy.server}")
	public String proxyUploadServer;

	@Value("${ftp.upload.proxy.port}")
	public String proxyUploadPort;

	@Value("${ftp.upload.proxy.user}")
	public String proxyUploadUsername;

	@Value("${ftp.upload.proxy.pass}")
	public String proxyUploadPassword;

	/////////
	@Value("${ftp.download.proxy.enable}")
	public String proxyDownloadEnable;
	
	@Value("${ftp.upload.proxy.enable}")
	public String proxyUploadEnable;

	////////////////////////
	@Value("${ftp.enable.commandline.log}")
	public String ftpEnableCommandlineLog;

	@Value("${ftp.downloader.enable}")
	public String ftpDownloaderEnable;

	@Value("${ftp.uploader.enable}")
	public String ftpUploaderEnable;
	
	@Value("${ftp.upload.localdir.backup.path}")
	public String ftpUploadLocalDirBackup;

	@Value("${ftp.upload.remotedir.backup.path}")
	public String ftpUploadRemoteDirBackup;

	
}
