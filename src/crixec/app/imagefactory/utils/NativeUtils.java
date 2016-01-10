package crixec.app.imagefactory.utils;

import crixec.app.imagefactory.ui.Toast;
import java.io.File;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.core.ExceptionHandler;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import crixec.app.imagefactory.core.Debug;

public class NativeUtils
{
	static{
		System.loadLibrary("bootimg");
	}

	public static boolean unpackbootimg(String bootImgFile, String outputPath, boolean mtk)
	{
		String[] args = new String[5];
		args[0] = "unpackbootimg";
		args[1] = "--input";
		args[2] = bootImgFile;
		args[3] = "--output";
		args[4] = outputPath;
		if (unpackbootimg(args) == 0)
		{
			File from = null, to = null;
			from = new File(outputPath, "ramdisk.cpio.gz");
			if (mtk)
			{
				to = new File(outputPath, "unrevise.ramdisk.cpio.gz");
				from.renameTo(to);
				try
				{
					FileInputStream fis = new FileInputStream(to);
					FileOutputStream fos = new FileOutputStream(from);
					FileOutputStream extras = new FileOutputStream(new File(to.getParent(), "mtk_extras"));

					byte[] buf = new byte[512];
					int code = fis.read(buf);
					if (code == 512)
					{
						extras.write(buf, 0, code);
						extras.flush();
						extras.close();
						buf = new byte[1024 * 1000];
						while ((code = fis.read(buf)) != -1)
						{
							fos.write(buf, 0, code);
							fos.flush();
						}
					}
					fos.close();
					fis.close();
					to.delete();

				}
				catch (Exception e)
				{
					ExceptionHandler.handle(e);
				}
			}
			if (unGzip(from.getAbsolutePath()))
			{
				uncpio(outputPath + "/ramdisk.cpio", outputPath + "/ramdisk", outputPath + "/cpio.list");
				return true;
			}
		}
		return false;
	}
	public static boolean repackbootimg(String outputDir, String outputFile)
	{
		String[] args = new String[15];
		try
		{
			args[0] = "repackbootimg";
			args[1] = "--output";
			args[2] = outputFile;
			args[3] = "--kernel";
			args[4] = outputDir + "/zImage";
			args[5] = "--ramdisk";
			args[6] = outputDir + "/ramdisk.cpio.gz";
			mkcpio(outputDir + "/cpio.list", outputDir + "/ramdisk.cpio");
			mkGzip(outputDir + "/ramdisk.cpio");
			File mtk = new File(outputDir, "mtk_extras");
			if (mtk.exists())
			{
				try
				{
					File from = new File(args[6]);
					File to = new File(from.getParent(), "unbind.ramdisk.cpio.gz");
					from.renameTo(to);
					FileOutputStream fos = new FileOutputStream(from);
					FileInputStream fis = new FileInputStream(mtk);
					byte[] buf = new byte[512];
					int code = fis.read(buf);
					if (code == 512)
					{
						fos.write(buf, 0, code);
						fos.flush();
						fis.close();
						fis = new FileInputStream(to);
						buf = new byte[1024 * 1000];
						while((code = fis.read(buf)) != -1){
							fos.write(buf, 0, code);
							fos.flush();
						}
					}
					fis.close();
					fos.close();
					to.delete();
				}
				catch (Exception e)
				{
					ExceptionHandler.handle(e);
				}
			}
			args[7] = "--pagesize";
			args[8] = FileUtils.readFile(outputDir + "/pagesize");
			args[9] = "--cmdline";
			args[10] = FileUtils.readFile(outputDir + "/cmdline");
			args[11] = "--base";
			args[12] = FileUtils.readFile(outputDir + "/base");
			args[13] = "--dt";
			args[14] = outputDir + "/dt.img";
		}
		catch (Exception e)
		{
			Toast.makeLongText(e.toString() + args.toString());
		}
		if (repackbootimg(args) == 0)
		{
			return true;
		}
		return false;
	}
	public static boolean simg2img(String from, String to){
		String[] args = new String[3];
		args[0] = "simg2img";
		args[1] = from;
		args[2] = to;
		if(simg2img(args) == 0){
			return true;
		}
		return false;
	}
	public static String sdat2img(String transfer, String newdat, String to){
		return ShellUtils.execSH(String.format("%s '%s' '%s' '%s'", new File(ImageFactory.getDataDirectory(), "sdat2img").getAbsolutePath(), transfer, newdat, to));
	}
	
	public static boolean unGzip(String from)
	{
		String[] args = new String[3];
		args[0] = "minigzip";
		args[1] = "-d";
		args[2] = from;
		if (minigzip(args) == 0)
		{
			return true;
		}
		return false;
	}
	public static boolean mkGzip(String from)
	{
		String[] args = new String[2];
		args[0] = "minigzip";
		args[1] = from;
		if (minigzip(args) == 0)
		{
			return true;
		}
		return false;
	}
	public static boolean unpackHuaweiApp(String file, String outdir)
	{
		String[] args = new String[3];
		args[0] = "unpackapp";
		args[1] = file;
		args[2] = outdir;
		if (unpackapp(args) == 0)
		{
			return true;
		}
		return false;
	}
	public static boolean unpackCoolpadApp(String file, String outdir)
	{
		String[] args = new String[2];
		args[0] = "unpackcpb";
		args[1] = file;
		args[2] = outdir;
		if (unpackcpb(args) == 0)
		{
			return true;
		}
		return false;
	}
	public static String mkcpio(String cpiolist, String outfile)
	{
		return ShellUtils.execSH(new File(ImageFactory.getDataDirectory(), "/mkcpio").getAbsolutePath() + " " + cpiolist + " " + outfile);
	}

	public static String uncpio(String cpiofile, String outdir, String outlist)
	{
		return ShellUtils.execSH(new File(ImageFactory.getDataDirectory(), "/uncpio").getAbsolutePath() + " " + cpiofile + " " + outdir + " " + outlist);
	}
	
	public static native int unpackbootimg(String[] args);

	public static native int repackbootimg(String[] args);

	public static native int unpackapp(String[] args);

	public static native int unpackcpb(String[] args);

	public static native int simg2img(String[] args);

	public static native String copyright();

	public static native int minigzip(String[] args);
	
}
