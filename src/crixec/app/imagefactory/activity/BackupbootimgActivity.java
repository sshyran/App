package crixec.app.imagefactory.activity;

import java.io.File;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.utils.PartitionUtils;
import crixec.app.imagefactory.utils.ShellUtils;

public class BackupbootimgActivity extends AppCompatActivity {
	private AppCompatEditText mDevPath;
	private AppCompatSpinner spinner;
	private AppCompatEditText outfile;
	private AppCompatButton mDoBackup;

	public static final String OUTPUT_PREFIX = "Kernel_Backup_";

	private Handler backupHandler = new Handler() {

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg) {
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2) {
				dialog = new ProgressDialog(BackupbootimgActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.backuping));
				dialog.show();
			} else {
				dialog.dismiss();
				File out = new File(UnpackbootimgActivity.getOutPath(), outfile.getText().toString() + ".img");
				if (msg.what == 0) {
					Dialog.build(BackupbootimgActivity.this).setTitle(R.string.backup_success)
						.setMessage(getString(R.string.backuped_to) + out.getAbsolutePath() + ".img").setPositiveButton(android.R.string.ok, null).show();
				}
				outfile.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[3]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_backup);
		spinner = (AppCompatSpinner) findViewById(R.id.activity_backup_sp_type);
		outfile = (AppCompatEditText) findViewById(R.id.activity_backup_et_outputfile);
		mDevPath = (AppCompatEditText) findViewById(R.id.activity_backup_et_devpath);
		mDoBackup = (AppCompatButton) findViewById(R.id.activity_backup_bt_do_backup);
		mDevPath.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				// TODO: Implement this method
			}

			@Override
			public void afterTextChanged(Editable p1) {
				// TODO: Implement this method
				mDoBackup.setEnabled(new File(p1.toString()).exists());
			}
		});
		mDevPath.setText(PartitionUtils.getKernelPath().getAbsolutePath());
		spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
				// TODO: Implement this method
				if (p3 == 0) {
					mDevPath.setText(PartitionUtils.getKernelPath().getAbsolutePath());
				} else {
					mDevPath.setText(PartitionUtils.getRecoveryPath().getAbsolutePath());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> p1) {
				// TODO: Implement this method
			}
		});
		spinner.setAdapter(getAdapter());
		outfile.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
		mDoBackup.setOnClickListener(new AppCompatButton.OnClickListener() {

			@Override
			public void onClick(View p1) {
				// TODO: Implement this method
				DoBackup db = new DoBackup();
				db.start();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Implement this method
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayAdapter getAdapter() {
		return new ArrayAdapter(BackupbootimgActivity.this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.mode_item_name));
	}

	public class DoBackup extends Thread {

		@Override
		public void run() {
			// TODO: Implement this method
			super.run();
			Looper.prepare();
			backupHandler.sendEmptyMessage(2);
			File out = new File(UnpackbootimgActivity.getOutPath(), outfile.getText().toString() + ".img");
			File dev = new File(mDevPath.getText().toString());
			if(!NativeUtils.cat(dev.getAbsolutePath(), out.getAbsolutePath())){
				backupHandler.sendEmptyMessage(1);
			}
			backupHandler.sendEmptyMessage(0);
		}

	}
}
