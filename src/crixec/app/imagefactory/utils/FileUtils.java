package crixec.app.imagefactory.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import crixec.app.imagefactory.core.ExceptionHandler;
import java.io.FileNotFoundException;
import crixec.app.imagefactory.core.Debug;

public class FileUtils {

	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String readFile(String file) {
		StringBuilder str = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			String line = br.readLine();
			if (line != null) {
				str.append(line);
			}
			while ((line = br.readLine()) != null) {
				str.append("\n" + line);
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str.toString();
	}

	public static int writeFile(InputStream is, File file) {
		int code = -1;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[is.available()];
			while ((code = is.read(buf)) != -1) {
				fos.write(buf);
				fos.flush();
			}
			is.close();
			fos.close();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		return code;
	}
	public static int writeFile(File from, File to){
		Debug.i(String.format("write file from=%s\tto=%s", from.getAbsolutePath(), to.getAbsolutePath()));
		try
		{
			return writeFile(new FileInputStream(from), to);
		}
		catch (FileNotFoundException e)
		{
			ExceptionHandler.handle(e);
		}
		return -1;
	}
}
