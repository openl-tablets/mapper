package org.openl.rules.mapping;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openl.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copies specified resource file to temp file, temp file is deleted when virtual machine
 * terminates.
 *
 * @author Ivan Holub
 */
public class FileExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(FileExtractor.class);

	private static final String TEMP_FILE_PREFIX = "mapping";

	private static final String TEMP_FILE_SUFFIX = ".xls";

	private FileExtractor() {
	}

	/**
	 * Copies specified resource file to temp file, temp file is deleted when virtual machine terminates.
	 * Returns the file as {@link File}.
	 *
	 * @param mapperClass the class on which {@link Class#getResourceAsStream(String)}
	 * is called to retrieve resource
	 * @param pathInJar path to resource
	 * @return the file {@link File}
	 * @throws IOException in case of error
	 */
	public static File extractFile(@SuppressWarnings("rawtypes") Class mapperClass, String pathInJar) throws IOException {
		InputStream is = mapperClass.getResourceAsStream(pathInJar);
		return copyFileStream(is);
	}

	/**
	 * Copies specified resource file to temp file, temp file is deleted when virtual machine terminates.
	 * The caller class {@link Class} is got by getCallerClass() defined in this class 
	 * Returns the file as {@link File}.
	 *
	 * @param pathInJar path to resource
	 * @return the file {@link File}
	 * @throws IOException in case of error
	 */
	public static File extractFile(String pathInJar) throws IOException {
		Class mappingClass = getCallerClass();
		return extractFile(mappingClass, pathInJar);
	}
	
	/**
	 * Copies file from specified InputStream {@link InputStream} to temp file, temp file is deleted when virtual machine terminates.
	 * Returns the file as {@link File}.
	 *
	 * @param is InputStream with resource
	 * @return the file {@link File}
	 * @throws IOException in case of error
	 */
	public static File copyFileStream(InputStream is) throws IOException {
		FileOutputStream out = null;
		try {
			File tempFile = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
			tempFile.deleteOnExit();
			out = new FileOutputStream(tempFile);
			IOUtils.copy(is, out);
			return tempFile;
		} finally {
			closeQuietly(is);
			closeQuietly(out);
		}
	}
	

	private static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			LOG.error("Error closing input stream", e);
		}
	}

	private static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException e) {
			LOG.error("Error closing output stream", e);
		}
	}
	
	private static Class getCallerClass() {
		// getStackTrace()[0] is the method we are in right now
		// getStackTrace()[1] is the caller method in this class
		// getStackTrace()[2] should be the caller's method outside
		return new Throwable().getStackTrace()[2].getClass();
	}

}