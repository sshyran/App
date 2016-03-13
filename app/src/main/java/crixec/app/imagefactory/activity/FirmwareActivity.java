package crixec.app.imagefactory.activity;
import java.io.File;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.ui.FileChooseDialog;

public class FirmwareActivity extends AppCompatActivity implements View.OnClickListener
{
	
	private AppCompatEditText etFirmware;
	private AppCompatButton btDoUnpack;
	private AppCompatSpinner spinner;
	private AppCompatButton btSelect;
	private AppCompatEditText etOutput;
	public static final String OUTOUT_PRRFIX = "Firmware_Out_";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[4]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_firmware);
		spinner = (AppCompatSpinner) findViewById(R.id.activity_firmware_sp_type);
		btDoUnpack = (AppCompatButton) findViewById(R.id.activity_firmware_bt_do_unpack);
		btSelect = (AppCompatButton) findViewById(R.id.activity_firmawre_bt_select);
		etFirmware = (AppCompatEditText) findViewById(R.id.activity_firmware_et_firmware_file);
		etOutput = (AppCompatEditText) findViewById(R.id.activity_firmware_et_outputpath);
		
		btDoUnpack.setOnClickListener(this);
		btSelect.setOnClickListener(this);
		spinner.setAdapter(getAdapter());
		btDoUnpack.setEnabled(false);
		etOutput.setText(OUTOUT_PRRFIX + DeviceUtils.getSystemTime());
		etFirmware.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					// TODO: Implement this method
					btDoUnpack.setEnabled(new File(p1.toString()).exists());
				}
		});
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayAdapter getAdapter(){
		return new ArrayAdapter(FirmwareActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.firmware_item_name));
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		if(item.getItemId() == android.R.id.home){
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.activity_firmawre_bt_select:{
					new FileChooseDialog(FirmwareActivity.this).choose(getResources().getStringArray(R.array.firmware_item_name)[spinner.getSelectedItemPosition()], new FileChooseDialog.Callback(){

							@Override
							public void onSelected(File file)
							{
								// TODO: Implement this method
								etFirmware.setText(file.getAbsolutePath());
							}
						});
				break;
			}
			case R.id.activity_firmware_bt_do_unpack:{
				new DoUnpack().start();
				break;
			}
		}
	}
@SuppressLint("HandlerLeak")
	private Handler unpackHandler = new Handler(){

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2)
			{
				dialog = new ProgressDialog(FirmwareActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.firmware_decompressing));
				dialog.show();
			}
			else
			{
				dialog.dismiss();
				if (msg.what == 0)
				{
					Dialog.build(FirmwareActivity.this)
						.setTitle(R.string.firmware_decomressed_success)
						.setMessage(getString(R.string.firmware_decompressed_to) + UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + etOutput.getText().toString())
						.setPositiveButton(android.R.string.ok, null)
						.show();
				}
				else
				{
					Toast.makeLongText(getString(R.string.firmware_decompressed_failure));
				}
				etOutput.setText(OUTOUT_PRRFIX + DeviceUtils.getSystemTime());
			}
		}

	};
	public class DoUnpack extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			Looper.prepare();
			unpackHandler.sendEmptyMessage(2);
			boolean b = false;
			if(spinner.getSelectedItemPosition() == 0){
				b = NativeUtils.unpackHuaweiApp(etFirmware.getText().toString(), UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + etOutput.getText().toString());
			} else {
				b = NativeUtils.unpackCoolpadApp(etFirmware.getText().toString(), UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" +etOutput.getText().toString());
			}
			if (b)
			{
				unpackHandler.sendEmptyMessage(0);
			}
			else
			{
				unpackHandler.sendEmptyMessage(1);
			}
		}

	}
}

