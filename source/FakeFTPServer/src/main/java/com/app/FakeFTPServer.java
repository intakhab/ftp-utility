package com.app;

import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

public class FakeFTPServer {
	public static void main(String[] args) {
		FakeFtpServer fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount("ias", "password", "c:\\data"));
        fakeFtpServer.setServerControlPort(21);  // use any free port

		FileSystem fileSystem = new WindowsFakeFileSystem();
		fileSystem.add(new DirectoryEntry("c:\\data"));
		fileSystem.add(new FileEntry("c:\\data\\file1.txt", "abcdef 1"));
		fileSystem.add(new FileEntry("c:\\data\\file2.txt", "abcdef 2"));
		fileSystem.add(new FileEntry("c:\\data\\file3.txt", "abcdef 3"));
		fileSystem.add(new FileEntry("c:\\data\\file4.txt", "abcdef 4"));
		fileSystem.add(new FileEntry("c:\\data\\file5.txt", "abcdef 5"));
		fileSystem.add(new FileEntry("c:\\data\\file6.txt", "abcdef 6"));


		//fileSystem.add(new FileEntry("c:\\data\\run.exe"));
		fakeFtpServer.setFileSystem(fileSystem);

		fakeFtpServer.start();


	}
}
