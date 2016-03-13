package crixec.app.imagefactory.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.core.ImageFactory;

public class ShellUtils
{
	public static String exec(String sh, String cmd, boolean doResult){
		StringBuilder s = new StringBuilder();
		Debug.i("Exec Commnd : " + cmd);
		try
		{
			Process p = Runtime.getRuntime().exec(sh);
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			p.waitFor();
			if (doResult)
			{
				String line = null;
				line = reader.readLine();
				if (line != null)
				{
					s.append(line);
				}
				while ((line = reader.readLine()) != null)
				{
					s.append("\n" + line);
				}
			}
			reader.close();
			dos.close();
		}
		catch (Exception e)
		{
			ExceptionHandler.handle(e);
		}
		return s.toString();
	}
	public static String exec(String cmd, boolean doResult)
	{
		return exec("su", cmd, doResult);
	}
	public static String exec(String cmd)
	{
		return exec(cmd, true);
	}
	public static String execSH(String cmd){
		return execSH(cmd, true);
	}
	public static String execSH(String cmd, boolean doResult){
		return exec("sh", cmd, doResult);
	}
	public static String chmod(int mode, File file){
		return exec("toolbox chmod -R " + mode + " " + file.getAbsolutePath());
	}
	public static String backup(File from, File to){
		StringBuilder sb = new StringBuilder();
		File tmp = ImageFactory.getTmpFile();
		sb.append(exec(String.format("toolbox cat '%s' > '%s'", from.getAbsolutePath(), tmp.getAbsolutePath())));
		sb.append(exec(String.format("toolbox chmod 0777 %s", tmp.getAbsolutePath())));
		FileUtils.writeFile(tmp, to);
		tmp.delete();
		return sb.toString();
	}
	public static String restore(File from, File to){
		StringBuilder sb = new StringBuilder();
		File tmp = ImageFactory.getTmpFile();
		FileUtils.writeFile(from, tmp);
		sb.append(exec(String.format("toolbox cat '%s' > '%s'", tmp.getAbsolutePath(), to.getAbsolutePath())));
		sb.append(exec(String.format("toolbox chmod 0777 %s", tmp.getAbsolutePath())));
		tmp.delete();
		return sb.toString();
	}
}
