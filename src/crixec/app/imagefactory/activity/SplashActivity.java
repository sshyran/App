package crixec.app.imagefactory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable(){

				@Override
				public void run()
				{
					// TODO: Implement this method
					Intent main = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(main);
					ImageFactory.forceStop();
				}
		}, ImageFactory.SPLASH_DELAY_TIME);
		AnalyticsConfig.setAppkey(SplashActivity.this, "567b804fe0f55a706a002485");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
