package crixec.app.imagefactory.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import crixec.app.imagefactory.core.ImageFactory;

@SuppressLint("NewApi")
public class ClipboardUtils {

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static CharSequence get() {
		return ((ClipboardManager) ImageFactory.APP.getSystemService(Context.CLIPBOARD_SERVICE)).getText();
	}

	@SuppressWarnings("deprecation")
	public static void set(CharSequence text) {
		((ClipboardManager) ImageFactory.APP.getSystemService(Context.CLIPBOARD_SERVICE)).setText(text);
	}
}
