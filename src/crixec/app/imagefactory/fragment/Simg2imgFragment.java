package crixec.app.imagefactory.fragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import crixec.app.imagefactory.R;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatButton;
import android.content.Context;
import crixec.app.imagefactory.utils.DeviceUtils;
import android.text.TextWatcher;
import android.content.Intent;
import crixec.app.imagefactory.core.ImageFactory;
import android.os.Handler;
import android.app.ProgressDialog;
import android.os.Message;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.activity.UnpackbootimgActivity;
import crixec.app.imagefactory.utils.NativeUtils;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.core.ExceptionHandler;
import android.text.Editable;
import java.io.File;
import android.app.Activity;
import crixec.app.imagefactory.ui.FileChooseDialog;

public class Simg2imgFragment extends Fragment implements View.OnClickListener
{
	private View root;
	private Context ctx;
	private AppCompatEditText etImage;
	private AppCompatEditText etOut;
	private AppCompatButton btSelect;
	private AppCompatButton btDoConvert;
	private String OUTPUT_PREFIX = "Convert_Out_";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
// TODO: Implement this method
		root = inflater.inflate(R.layout.fragment_imageconvert_simg2img, container, false);
		ctx = root.getContext();
		etImage = (AppCompatEditText) findViewById(R.id.fragment_imageconvert_simg2img_et_path);
		etOut = (AppCompatEditText) findViewById(R.id.fragment_imageconvert_simg2img_et_outputpath);
		btSelect = (AppCompatButton) findViewById(R.id.fragment_imageconvert_simg2img_bt_select);
		btDoConvert = (AppCompatButton) findViewById(R.id.fragment_imageconvert_simg2img_bt_do_convert);
		btDoConvert.setEnabled(false);
		btDoConvert.setOnClickListener(this);
		btSelect.setOnClickListener(this);
		etOut.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
		etImage.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
				{
				}

				@Override
				public void afterTextChanged(Editable p1)
				{
					btDoConvert.setEnabled(new File(p1.toString()).exists());
				}
			});
		return root;
	}
	public View findViewById(int id)
	{
		return root.findViewById(id);
	}
	@Override
	public void onClick(View p1)
	{
		switch (p1.getId())
		{
			case R.id.fragment_imageconvert_simg2img_bt_select:{
					FileChooseDialog dialog = new FileChooseDialog(ctx);
					dialog.choose("SIMG2IMG", new FileChooseDialog.Callback(){

							@Override
							public void onSelected(File file)
							{
								// TODO: Implement this method
								etImage.setText(file.getAbsolutePath());
							}
					});
					break;
				}
			case R.id.fragment_imageconvert_simg2img_bt_do_convert:{
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
				dialog = new ProgressDialog(ctx);
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
					Dialog.build(ctx)
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
			try
			{
				if (!NativeUtils.simg2img(etImage.getText().toString(), UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + etOut.getText().toString() + ".img"))
				{
					convertHandler.sendEmptyMessage(1);
					return;
				}
			}
			catch (Exception e)
			{
				ExceptionHandler.handle(e);
				convertHandler.sendEmptyMessage(1);
				return;
			}
			convertHandler.sendEmptyMessage(0);
		}

	}
}
