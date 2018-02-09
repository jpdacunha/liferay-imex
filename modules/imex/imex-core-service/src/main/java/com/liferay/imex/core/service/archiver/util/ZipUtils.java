package com.liferay.imex.core.service.archiver.util;

import com.liferay.portal.kernel.log.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

	private static final int BUFFER = 2048;

	private Log log;

	public ZipUtils(Log log) {
		this.log = log;
	}

	@SuppressWarnings("rawtypes")
	public void unzipArchive(File archive, File outputDir) throws IOException {
		try {
			ZipFile zipfile = new ZipFile(archive);
			for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				unzipEntry(zipfile, entry, outputDir);
			}
		} catch (Exception e) {
			throw new IOException("Error while extracting file " + archive, e);
		}
	}

	private void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir)
			throws IOException {

		if (entry.isDirectory()) {
			createDir(new File(outputDir, entry.getName()));
			return;
		}

		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			createDir(outputFile.getParentFile());
		}

		log.debug("Extracting: " + entry);
		BufferedInputStream inputStream = new BufferedInputStream(zipfile
				.getInputStream(entry));
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(outputFile));

		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}

	private void createDir(File dir) {
		log.debug("Creating dir " + dir.getName());
		if (!dir.mkdirs())
			throw new RuntimeException("Can not create dir " + dir);
	}

	public void _unzipArchive(File archive, File outputDir) throws IOException {
		
		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(archive);
		CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
				checksum));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			log.debug("Extracting: " + entry);
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			FileOutputStream fos = new FileOutputStream(entry.getName());
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = zis.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}
		zis.close();
		log.debug("Checksum:" + checksum.getChecksum().getValue());

	}

	public void zipFiles(File archive, File inputDir) throws IOException {

		FileOutputStream dest = new FileOutputStream(archive);
		CheckedOutputStream checksum = new CheckedOutputStream(dest,
				new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				checksum));

		zipEntries(out, inputDir, inputDir);
		
		out.close();
		log.debug("checksum:" + checksum.getChecksum().getValue());

	}

	private void zipEntries(ZipOutputStream out, File file, File inputDir)
			throws IOException {
		String name;
		if (file.equals(inputDir)) {
			name = "";
		} else {
			name = file.getPath().substring(inputDir.getPath().length() + 1);
		}
		if (File.separatorChar == '\\') {
			name = name.replace("\\","/" );
		}
	
		log.debug("Adding: " + name);
		
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				zipEntries(out, child, inputDir);
			}
		} else {
			ZipEntry entry = new ZipEntry(name);
			out.putNextEntry(entry);
			IOUtils.copy(FileUtils.openInputStream(file), out);
		}
	}

}
