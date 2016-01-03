package crixec.app.imagefactory.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import crixec.app.imagefactory.core.ImageFactory;

public class DeviceUtils {
	@SuppressLint("SimpleDateFormat")
	public static String getSystemTime() {
		return new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
	}

	public static String getSystemModel() {
		return android.os.Build.MODEL;
	}

	public static String getSystemManufacture() {
		return android.os.Build.MANUFACTURER;
	}

	public static int getSystemSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static String getSystemARCH() {
		return ShellUtils.execSH("toolbox getprop ro.product.cpu.abi");
	}

	public static boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) ImageFactory.APP
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isWifiConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) ImageFactory.APP
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
			return mWiFiNetworkInfo.isAvailable();
		}

		return false;
	}

	public static boolean isMobileConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) ImageFactory.APP
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mMobileNetworkInfo != null) {
			return mMobileNetworkInfo.isAvailable();
		}

		return false;
	}

	public static boolean hasNetwork() {
		if (isNetworkConnected()) {
			if (isWifiConnected() || isMobileConnected()) {
				return true;
			}
		}
		return false;
	}
}
