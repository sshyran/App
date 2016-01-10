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
		ImageFactory.getStorageDirectory().mkdirs();
		ImageFactory.getDataDirectory().mkdirs();
		File mkcpio = new File(ImageFactory.getDataDirectory(), "/mkcpio");
		File uncpio = new File(ImageFactory.getDataDirectory(), "/uncpio");
		File sdat2img = new File(ImageFactory.getDataDirectory(), "/sdat2img");
		try
		{
			if(!mkcpio.exists() && !mkcpio.canExecute()){
				FileUtils.writeFile(mContext.getAssets().open(DeviceUtils.getSystemARCH() + "/mkcpio"), mkcpio);
				mkcpio.setExecutable(true, false);
			}
			if(!uncpio.exists() && !uncpio.canExecute()){
				FileUtils.writeFile(mContext.getAssets().open(DeviceUtils.getSystemARCH() + "/uncpio"), uncpio);
				uncpio.setExecutable(true, false);
			}
			if(!sdat2img.exists() && !sdat2img.canExecute()){
				FileUtils.writeFile(mContext.getAssets().open(DeviceUtils.getSystemARCH() + "/sdat2img"), sdat2img);
				sdat2img.setExecutable(true, false);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.handle(e);
		}
		Debug.i("AppCheckTask stopped");
	}
}
