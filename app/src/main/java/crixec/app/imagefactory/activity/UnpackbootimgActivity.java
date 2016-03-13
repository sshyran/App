package crixec.app.imagefactory.activity;
import android.os.Bundle;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;
import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatButton;
import crixec.app.imagefactory.utils.DeviceUtils;
import java.io.File;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.ui.Dialog;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import crixec.app.imagefactory.core.ExceptionHandler;
import android.text.TextWatcher;
import android.text.Editable;
import crixec.app.imagefactory.ui.FileChooseDialog;

public class UnpackbootimgActivity extends AppCompatActivity implements View.OnClickListener
{
	private AppCompatEditText etBootimg;
	private AppCompatEditText etOutput;
	private AppCompatCheckBox cbMtk;
	private AppCompatButton btSelect;
	private AppCompatButton btUnpack;
	private ProgressDialog dialog;
	private Handler unpackHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2)
			{
				dialog = new ProgressDialog(UnpackbootimgActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.unpacking));
				dialog.show();
			}
			else
			{
				dialog.dismiss();
				File out = new File(getOutPath(), etOutput.getText().toString());
				if (msg.what == 0)
				{
					Dialog.build(UnpackbootimgActivity.this)
						.setTitle(R.string.unpack_success)
						.setMessage(getString(R.string.unpack_kernel_to) + out.getAbsolutePath())
						.setPositiveButton(android.R.string.ok, null)
						.show();
				}
				else
				{
					Toast.makeLongText(getString(R.string.unpack_failure));
				}
				etOutput.setText(OUTOUT_PRRFIX + DeviceUtils.getSystemTime());
			}
		}

	};
	public static final String OUTOUT_PRRFIX = "Kernel_Out_";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[0]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_unpackbootimg);
		init();
	}
	private void init()
	{
		etBootimg = (AppCompatEditText) findViewById(R.id.activity_unpackbootimg_et_path);
		etOutput = (AppCompatEditText) findViewById(R.id.activity_unpackbootimg_et_outputpath);
		cbMtk = (AppCompatCheckBox) findViewById(R.id.activity_unpackbootimg_cb_is_mtk);
		btSelect = (AppCompatButton) findViewById(R.id.activity_unpackbootimg_bt_select);
		btUnpack = (AppCompatButton) findViewById(R.id.activity_unpackbootimg_bt_do_unpack);
		btSelect.setOnClickListener(this);
		btUnpack.setOnClickListener(this);
		btUnpack.setEnabled(false);
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
					// TODO: Implement this method
					btUnpack.setEnabled(new File(p1.toString()).isFile());
				}
		});
		etOutput.setText(OUTOUT_PRRFIX + DeviceUtils.getSystemTime());

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		if (item.getItemId() == android.R.id.home)
		{
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

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch (p1.getId())
		{
			case R.id.activity_unpackbootimg_bt_select:{
					FileChooseDialog dialog = new FileChooseDialog(UnpackbootimgActivity.this);
					dialog.choose("boot.img & recovery.img", new FileChooseDialog.Callback(){

							@Override
							public void onSelected(File file)
							{
								// TODO: Implement this method
								etBootimg.setText(file.getAbsolutePath());
							}
						});

					break;
				}
			case R.id.activity_unpackbootimg_bt_do_unpack:{
					DoUnpack t = new DoUnpack();
					try
					{
						t.start();
					}
					catch (Exception e)
					{
						ExceptionHandler.handle(e);
					}
					break;
				}
		}
	}
	public static File getOutPath()
	{
		File f = new File(ImageFactory.getStorageDirectory(), "Workspace");
		//File f = new File("/data/data/" + ImageFactory.getAppPackageName(), "BOOT");
		f.mkdirs();
		return f;
	}
	public class DoUnpack extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			Looper.prepare();
			unpackHandler.sendEmptyMessage(2);
			File out = new File(getOutPath(), etOutput.getText().toString());
			boolean b = NativeUtils.unpackbootimg(etBootimg.getText().toString(), out.getAbsolutePath(), cbMtk.isChecked());
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


