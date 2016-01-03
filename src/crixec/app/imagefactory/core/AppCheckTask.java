package crixec.app.imagefactory.core;
import java.io.File;

import android.content.Context;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.FileUtils;

public class AppCheckTask extends Thread
{
	private Context mContext;
	
	public AppCheckTask(Context ctx){
		this.mContext = ctx;
	}
	
	@Override
	public void run()
	{
		// TODO: Implement this method
		super.run();
		Debug.i("AppCheckTask starting");
		Debug.i("建立工作目录:" + ImageFactory.getStorageDirectory().getAbsolutePath());
		ImageFactory.getStorageDirectory().mkdirs();
		ImageFactory.getDataDirectory().mkdirs();
		File mkcpio = new File(ImageFactory.getDataDirectory(), "/mkcpio");
		Debug.i("mkcpio => " + mkcpio.getAbsolutePath());
		File uncpio = new File(ImageFactory.getDataDirectory(), "/uncpio");
		Debug.i("uncpio => " + uncpio.getAbsolutePath());
		try
		{
			if(!mkcpio.exists() && !mkcpio.canExecute()){
				Debug.i("Copy file : " + DeviceUtils.getSystemARCH() + "/mkcpio");
				FileUtils.writeFile(mContext.getAssets().open(DeviceUtils.getSystemARCH() + "/mkcpio"), mkcpio);
				mkcpio.setExecutable(true, false);
			}
			if(!uncpio.exists() && !uncpio.canExecute()){
				Debug.i("Copy file : " + DeviceUtils.getSystemARCH() + "/uncpio");
				FileUtils.writeFile(mContext.getAssets().open(DeviceUtils.getSystemARCH() + "/uncpio"), uncpio);
				uncpio.setExecutable(true, false);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.handle(e);
		}
		Debug.i("AppCheckTask stopped");
	}
}
