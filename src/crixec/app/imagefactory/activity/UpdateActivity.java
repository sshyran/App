package crixec.app.imagefactory.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.server.CheckUpdateServerBean;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.HttpUtils;
import java.io.RandomAccessFile;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
	private AppCompatTextView tvCurrentVer;
	private AppCompatButton btCheck;
	private AppCompatButton btDo;
	private CheckUpdateServerBean bean;
	private AppCompatTextView tvNews;
	private AppCompatTextView tvNewVer;
	@SuppressLint("HandlerLeak")
	private Handler checkHandler = new Handler() {

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg) {
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2) {
				dialog = new ProgressDialog(UpdateActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.update_checking));
				dialog.show();
			} else if (msg.what == 1) {

				Toast.makeShortText(getString(R.string.no_available_network));
			} else if (msg.what == 3) {
				dialog.dismiss();
				Toast.makeShortText("");
			} else {
				dialog.dismiss();
				if (msg.what == 0) {
					if (bean.getVersionCode() > ImageFactory.getPackageVersionCode()) {
						Toast.makeShortText(getString(R.string.has_new_version));
						btDo.setEnabled(true);
						tvNewVer.setText(bean.getVersionName() + "(" + bean.getVersionCode() + ")");
						tvNews.setText(bean.BIND);
					} else {
						Toast.makeShortText(getString(R.string.current_is_newest));
					}
				} else {
					Toast.makeLongText(getString(R.string.update_check_failure));
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.drawer_item_name)[1]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_update);
		tvCurrentVer = (AppCompatTextView) findViewById(R.id.activity_update_tv_current_version);
		tvCurrentVer.setText(ImageFactory.getPackageVersionName() + "(" + ImageFactory.getPackageVersionCode() + ")");
		btDo = (AppCompatButton) findViewById(R.id.activity_update_bt_do_update);
		btDo.setEnabled(false);
		btDo.setOnClickListener(this);
		btCheck = (AppCompatButton) findViewById(R.id.activity_update_bt_check_update);
		btCheck.setOnClickListener(this);
		tvNews = (AppCompatTextView) findViewById(R.id.activity_update_tv_upgrade_content);
		tvNewVer = (AppCompatTextView) findViewById(R.id.activity_update_tv_newest_version);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Implement this method
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View p1) {
		// TODO: Implement this method
		if (p1.getId() == R.id.activity_update_bt_check_update) {
			new CheckUpdate().start();
		} else if (p1.getId() == R.id.activity_update_bt_do_update) {
			Toast.makeShortText(getString(R.string.start_upgrade));
			DownloadApk da = new DownloadApk();
			da.execute(new String[] { bean.getApkUrl() });
		}
	}

	public class CheckUpdate extends Thread {

		@Override
		public void run() {
			// TODO: Implement this method
			super.run();
			if (!DeviceUtils.hasNetwork()) {
				checkHandler.sendEmptyMessage(1);
				return;
			}
			checkHandler.sendEmptyMessage(2);
			try {
				String json = HttpUtils.doGet(ImageFactory.SERVER_URL + "?method=2");
				bean = CheckUpdateServerBean.parseJson(json);
				bean.BIND = HttpUtils.doGet(bean.getChangeLogUrl());
			} catch (Exception e) {
				ExceptionHandler.handle(e);
				checkHandler.sendEmptyMessage(3);
				return;
			}
			checkHandler.sendEmptyMessage(0);
		}
	}

	public class DownloadApk extends AsyncTask<String, Integer, String> {
		ProgressDialog dialog;
		int countSize = 0;
		int dlSize = 0;
		File localFile;
		URL url;
		URLConnection coon;
		InputStream is;
		RandomAccessFile fos;

		@Override
		protected void onPostExecute(String result) {
			// TODO: Implement this method
			super.onPostExecute(result);
			dialog.dismiss();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(localFile), "application/vnd.android.package-archive");
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			// TODO: Implement this method
			super.onCancelled();
			dialog.dismiss();
		}

		@Override
		protected void onCancelled(String result) {
			// TODO: Implement this method
			super.onCancelled(result);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO: Implement this method
			super.onPreExecute();
			dialog = new ProgressDialog(UpdateActivity.this);
			dialog.setTitle(R.string.download_upgrade);
			dialog.setCancelable(false);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setProgress(0);
			dialog.setMessage(getString(R.string.connecting_to_file));
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer[] values) {
			// TODO: Implement this method
			super.onProgressUpdate(values);
			if (values.length > 0) {
				dialog.setMax(countSize);
			}
			dialog.setProgress(dlSize);
			dialog.setMessage(getString(R.string.downloading));
		}

		@Override
		protected String doInBackground(String[] p1) {
			// TODO: Implement this method
			try {
				Debug.i("Download file:" + p1[0]);
				url = new URL(p1[0]);
				coon = url.openConnection();
				is = coon.getInputStream();
				coon.connect();
				localFile = new File(ImageFactory.getStorageDirectory(),
						"Update_" + bean.getVersionName() + "_" + bean.getVersionCode() + ".apk");
				fos = new RandomAccessFile(localFile, "rw");
				byte[] buf = new byte[1024 * 4096];
				int code = -1;
				countSize = coon.getContentLength();
				if(is.available() == fos.length()){
					return "下载完成";
				}
				publishProgress(new Integer[] { countSize });
				while ((code = is.read(buf)) != -1) {
					fos.write(buf, 0, code);
					dlSize += code;
					publishProgress(new Integer[0]);
				}
				is.close();
				fos.close();
			} catch (Exception e) {
				ExceptionHandler.handle(e);
				return "下载失败";
			}
			return "下载完成";
		}

		public int divide(long l1, long l2) {
			double d1 = Double.valueOf(l1);
			double d2 = Double.valueOf(l2);
			return (int) (d1 / d2);
		}
	}
}
