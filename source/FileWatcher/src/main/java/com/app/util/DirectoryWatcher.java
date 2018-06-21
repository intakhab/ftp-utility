package com.app.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DirectoryWatcher {
	private static final Logger logger = LogManager.getLogger("DirectoryWatcher");

	public static void scanDir() {

		try {
			logger.info("Starting this app");
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("c:/data");
			Path desDir = Paths.get("c:/data2");

			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			while (true) {
				WatchKey key;
				try {
					// wait for a key to be available
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					// get event type
					WatchEvent.Kind<?> kind = event.kind();

					// get file name
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					if (kind == OVERFLOW) {
						continue;
					} else if (kind == ENTRY_CREATE) {
						System.out.println(kind.name() + ": " + fileName);
						//copyFile("c:/data/" + fileName.toString() , desDir.toString());
						move_file("c:/data/" + fileName.toString() , desDir.toString());
						Files.delete(dir);
					}
				}

				// IMPORTANT: The key must be reset after processed
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private static void copyFiles(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}

	/**
	 * 
	 * @param filePath
	 * @param dir
	 */
	public static void copyFile(String filePath, String dir) {
		Path sourceFile = Paths.get(filePath);
		Path targetDir = Paths.get(dir);
		Path targetFile = targetDir.resolve(sourceFile.getFileName());

		try {

			Files.copy(sourceFile, targetFile);
			Files.deleteIfExists(sourceFile);
			System.out.format("Copied  file in %s direcotry.", dir);

		} catch (FileAlreadyExistsException ex) {
			System.err.format("File %s already exists.", targetFile);
			scanDir();
		} catch (IOException ex) {
			System.err.format("I/O Error when copying file " + ex.getMessage());
			scanDir();
		}
	}
	
	public static void move_file(String local,String dest) throws IOException {
	    Path fileToMovePath = Files.createFile(Paths.get(local + new Date() + ".txt"));
	    Path targetPath = Paths.get(dest);
	    Files.move(fileToMovePath, targetPath.resolve(fileToMovePath.getFileName()));
	}
	
	

	public static void main(String[] args) {
		try {
			FileUtils.moveFile(
				      FileUtils.getFile("c:/data/"), 
				      FileUtils.getFile("c:/data2/2.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}