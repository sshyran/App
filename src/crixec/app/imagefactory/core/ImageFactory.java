package crixec.app.imagefactory.core;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.utils.XmlDataUtils;

public class ImageFactory extends Application {
	public static final long SPLASH_DELAY_TIME = 1500;
	public static Application APP;
	public static final String STORAGE_NAME = "ImageFactory";
	private static PackageInfo mPackageInfo;
	public static final int FILECHOOSE_CODE_REQUEST = 1005;
	public static boolean DO_DEBUG = true;
	public static String SERVER_URL = "http://crixec.xyz/Crixec/App/ImageFactory/index.php";

	@Override
	public void onCreate() {
		// TODO: Implement this method
		super.onCreate();
		APP = this;
		XmlDataUtils.getInstance().init();
		new CrashHandler(getApplicationContext()).init();
		try {
			mPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			ExceptionHandler.handle(e);
		}
		new AppCheckTask(this).start();
	}

	public static void forceStop() {
		forceStop(0);
	}

	public static void forceStop(long l) {
		try {
			Thread.sleep(l);
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Context getApp() {
		// TODO: Implement this method
		return APP;
	}

	public static String getAppPackageName() {
		return APP.getPackageName();
	}

	public static PackageManager getAppPackageManager() {
		return APP.getPackageManager();
	}

	public static PackageInfo getPackageInfo() {
		return mPackageInfo;
	}

	public static String getPackageVersionName() {
		return mPackageInfo.versionName;
	}

	public static int getPackageVersionCode() {
		return mPackageInfo.versionCode;
	}

	@SuppressLint("SdCardPath")
	public static File getStorageDirectory() {
		if ("".equals(XmlDataUtils.getString(APP.getString(R.string.keyname_workpath)))) {
			File file = null;
			File[] files = new File[] { Environment.getExternalStorageDirectory(), new File("/storage/sdcard1"),
					new File("/storage/sdcard0"), new File("/storage/emulated/0"), new File("/storage/emulated/legacy"),
					new File("/mnt/sdcard"), new File("/sdcard"), getDataDirectory() };
			for (File f : files) {
				file = new File(f, STORAGE_NAME);
				Debug.i("测试工作目录 ->" + file.getAbsolutePath());
				if (NativeUtils.canBeWorkspace(file)) {
					break;
				}
			}
			XmlDataUtils.putString(APP.getString(R.string.keyname_workpath), file.getAbsolutePath());
		} else {
			File file = new File(XmlDataUtils.getString(APP.getString(R.string.keyname_workpath)));
			if (!NativeUtils.canBeWorkspace(file)) {
				XmlDataUtils.remove(APP.getString(R.string.keyname_workpath));
				return getStorageDirectory();
			}
		}
		return new File(XmlDataUtils.getString(APP.getString(R.string.keyname_workpath)));
	}

	public static File getDataDirectory() {
		return APP.getFilesDir();
	}

}
