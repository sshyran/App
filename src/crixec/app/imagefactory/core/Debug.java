package crixec.app.imagefactory.core;
import java.io.File;
import java.io.FileWriter;

import android.util.Log;
import crixec.app.imagefactory.utils.DeviceUtils;

public class Debug
{
	public static String LOG_TAG = "ImageFactory-DEBUG";
	public static void e(CharSequence text)
	{
		if (ImageFactory.DO_DEBUG)
		{
			Log.e(LOG_TAG, text.toString());
		}
	}
	public static void w(CharSequence text)
	{
		if (ImageFactory.DO_DEBUG)
		{
			Log.w(LOG_TAG, text.toString());
		}
	}
	public static void i(CharSequence text)
	{
		if (ImageFactory.DO_DEBUG)
		{
			Log.i(LOG_TAG, text.toString());
		}
	}
	public static void v(CharSequence text)
	{
		if (ImageFactory.DO_DEBUG)
		{
			Log.v(LOG_TAG, text.toString());
		}
	}
	public static void writeLog(CharSequence text)
	{
		try
		{
			File dir = new File(ImageFactory.getDataDirectory(), "log");
			dir.mkdirs();
			File log = new File(dir , "Crash_" + DeviceUtils.getSystemTime() + ".log");
			Debug.i(log.getAbsolutePath());
			FileWriter fw = new FileWriter(log);
			fw.write(text.toString());
			fw.flush();
			fw.close();

		}
		catch (Exception e)
		{
			ExceptionHandler.handle(e);
		}
	}
}
