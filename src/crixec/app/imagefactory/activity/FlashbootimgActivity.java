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
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.utils.PartitionUtils;
import crixec.app.imagefactory.utils.RebootUtils;
import crixec.app.imagefactory.utils.ShellUtils;

public class FlashbootimgActivity extends AppCompatActivity
{
	
	
	@SuppressLint("HandlerLeak")
	private Handler flashHandler = new Handler(){

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2)
			{
				dialog = new ProgressDialog(FlashbootimgActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.flashing));
				dialog.show();
			}
			else
			{
				dialog.dismiss();
				if (msg.what == 0)
				{
					Dialog.build(FlashbootimgActivity.this)
						.setTitle(R.string.flash_success)
						.setMessage(getString(R.string.flash_kernel_from) + etBootimg.getText().toString())
						.setPositiveButton(android.R.string.ok, null)
						.show();
					if(cbAutoReboot.isChecked()){
						int i = spinner.getSelectedItemPosition();
						if(i == 0){
							RebootUtils.reboot();
						} else {
							RebootUtils.recovery();
						}
					}
				}
			}
		}

	};
	
	private AppCompatEditText etBootimg;
	private AppCompatEditText etTarget;
	private AppCompatButton btDoFlash;
	private AppCompatSpinner spinner;
	private AppCompatCheckBox cbAutoReboot;
	private AppCompatButton btSelect;
	private boolean b1 = false;
	private boolean b2 = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[2]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_flash);
		etBootimg = (AppCompatEditText) findViewById(R.id.activity_flash_et_bootimg);
		etTarget = (AppCompatEditText) findViewById(R.id.activity_flash_et_target);
		btDoFlash = (AppCompatButton) findViewById(R.id.activity_flash_bt_do_flash);
		btDoFlash.setEnabled(false);
		cbAutoReboot = (AppCompatCheckBox) findViewById(R.id.activity_flash_cb_auto_reboot);
		
		btSelect = (AppCompatButton) findViewById(R.id.activity_flash_bt_select);
		btSelect.setOnClickListener(new AppCompatButton.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					startActivityForResult(new Intent(FlashbootimgActivity.this, ChooserActivity.class), ImageFactory.FILECHOOSE_CODE_REQUEST);
				}
			});
		etTarget.addTextChangedListener(new TextWatcher(){

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
					b1 = new File(p1.toString()).exists();
					if(b1 && b2){
						btDoFlash.setEnabled(true);
					}
				}
			});
		etTarget.setText(PartitionUtils.getKernelPath().getAbsolutePath());
		etBootimg.addTextChangedListener(new TextWatcher(){

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
					// TODO: Implement this metho
					b2 = new File(p1.toString()).exists();
					if(b1 && b2){
						btDoFlash.setEnabled(true);
					}
				}
			});
				spinner = (AppCompatSpinner) findViewById(R.id.activity_flash_sp_type);
		spinner.setAdapter(getAdapter());
		spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					if(p3 == 0){
						etTarget.setText(PartitionUtils.getKernelPath().getAbsolutePath());
					} else {
						etTarget.setText(PartitionUtils.getRecoveryPath().getAbsolutePath());
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
				}
			});
		spinner.setAdapter(getAdapter());
		btDoFlash.setOnClickListener(new AppCompatButton.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					DoFlash db = new DoFlash();
					db.start();
				}
			});
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO: Implement this method
		if (resultCode == RESULT_CANCELED) return;
		switch (requestCode)
		{
			case ImageFactory.FILECHOOSE_CODE_REQUEST:{
					etBootimg.setText(data.getStringExtra("SELECTED"));
					break;
				}
			default:{
					break;
				}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayAdapter getAdapter(){
		return new ArrayAdapter(FlashbootimgActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mode_item_name));
	}
	public class DoFlash extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			Looper.prepare();
			flashHandler.sendEmptyMessage(2);
			String shell = "toolbox dd if=" + etBootimg.getText().toString() + " of=" + etTarget.getText().toString();
			ShellUtils.exec(shell);
			if(!NativeUtils.cat(etBootimg.getText().toString(), etTarget.getText().toString())){
				flashHandler.sendEmptyMessage(1);
			}
			flashHandler.sendEmptyMessage(0);
		}

	}
}

