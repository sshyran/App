package crixec.app.imagefactory.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import crixec.app.imagefactory.R;
import android.view.MenuItem;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import crixec.app.imagefactory.core.ImageFactory;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.utils.DeviceUtils;
import android.text.TextWatcher;
import android.text.Editable;
import java.io.File;

public class ImageConvertActivity extends AppCompatActivity implements View.OnClickListener
{
	
	private AppCompatEditText etImage;
	private AppCompatEditText etOut;
	private AppCompatButton btSelect;
	private AppCompatButton btDoConvert;
	private String OUTPUT_PREFIX = "Convert_Out_";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[5]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_imageconvert);
		etImage = (AppCompatEditText) findViewById(R.id.activity_imageconvert_et_path);
		etOut = (AppCompatEditText) findViewById(R.id.activity_imageconvert_et_outputpath);
		btSelect = (AppCompatButton) findViewById(R.id.activity_imageconvert_bt_select);
		btDoConvert = (AppCompatButton) findViewById(R.id.activity_imageconvert_bt_do_convert);
		btDoConvert.setEnabled(false);
		btDoConvert.setOnClickListener(this);
		btSelect.setOnClickListener(this);
		etOut.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
		etImage.addTextChangedListener(new TextWatcher(){

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
					btDoConvert.setEnabled(new File(p1.toString()).exists());
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
					etImage.setText(data.getStringExtra("SELECTED"));
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
		switch(p1.getId()){
			case R.id.activity_imageconvert_bt_select:{
					startActivityForResult(new Intent(ImageConvertActivity.this, ChooserActivity.class), ImageFactory.FILECHOOSE_CODE_REQUEST);
					break;
				}
			case R.id.activity_imageconvert_bt_do_convert:{
					new DoConvert().start();
					break;
				}
		}
	}


	private Handler convertHandler = new Handler(){

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2)
			{
				dialog = new ProgressDialog(ImageConvertActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.converting));
				dialog.show();
			}
			else
			{
				dialog.dismiss();
				if (msg.what == 0)
				{
					Dialog.build(ImageConvertActivity.this)
						.setTitle(R.string.converted_success)
						.setMessage(getString(R.string.converted_to) + UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + etOut.getText().toString() + ".img")
						.setPositiveButton(android.R.string.ok, null)
						.show();
				}
				else
				{
					Toast.makeLongText(getString(R.string.converted_failure));
				}
				etOut.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
			}
		}

	};
	public class DoConvert extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			convertHandler.sendEmptyMessage(2);
			try{
				if(!NativeUtils.simg2img(etImage.getText().toString(), UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + etOut.getText().toString() + ".img")){
					convertHandler.sendEmptyMessage(1);
					return;
				}
			}catch(Exception e){
				ExceptionHandler.handle(e);
				convertHandler.sendEmptyMessage(1);
			}
			convertHandler.sendEmptyMessage(0);
		}

	}
}

