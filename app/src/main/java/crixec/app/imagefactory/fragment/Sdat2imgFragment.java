package crixec.app.imagefactory.fragment;
import android.app.Fragment;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import crixec.app.imagefactory.R;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatButton;
import android.content.Context;
import android.text.TextWatcher;
import android.text.Editable;
import java.io.File;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.utils.DeviceUtils;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.activity.UnpackbootimgActivity;
import java.io.IOException;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.utils.NativeUtils;

public class Sdat2imgFragment extends Fragment implements AppCompatButton.OnClickListener{
	private View root;
	private Context ctx;
	private AppCompatEditText etTransfer;
	private AppCompatEditText etNewDat;
	private AppCompatEditText etOut;
	private AppCompatButton btSelectTransfer;
	private AppCompatButton btSelectNewDat;
	private AppCompatButton btDoConvert;
	private String OUTPUT_PREFIX = "Convert_Out_";
	private boolean b1 = false, b2 = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		root = inflater.inflate(R.layout.fragment_imageconvert_sdat2img, container, false);
		ctx = root.getContext();
		etTransfer = (AppCompatEditText) findViewById(R.id.fragment_imageconvert_sdat2img_et_transfer);
		etNewDat = (AppCompatEditText) findViewById(R.id.fragment_imageconvert_sdat2img_et_new_dat);
		etOut = (AppCompatEditText) findViewById(R.id.fragment_imageconvert_sdat2img_et_outputpath);
		btSelectTransfer = (AppCompatButton) findViewById(R.id.fragment_imageconvert_sdat2img_bt_select_transfer);
		btSelectNewDat = (AppCompatButton) findViewById(R.id.fragment_imageconvert_sdat2img_bt_select_new_dat);
		btDoConvert = (AppCompatButton) findViewById(R.id.fragment_imageconvert_sdat2img_bt_do_convert);
		etTransfer.addTextChangedListener(new TextWatcher(){

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
						btDoConvert.setEnabled(true);
					}
				}
			});
		etNewDat.addTextChangedListener(new TextWatcher(){

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
					b2 = new File(p1.toString()).exists();
					if(b1 && b2){
						btDoConvert.setEnabled(true);
					}
				}
			});
		btSelectTransfer.setOnClickListener(this);
		btSelectNewDat.setOnClickListener(this);
		btDoConvert.setOnClickListener(this);
		btDoConvert.setEnabled(false);
		etOut.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
		return root;
	}
	public View findViewById(int id){
		return root.findViewById(id);
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.fragment_imageconvert_sdat2img_bt_do_convert:{
				new DoConvert().start();
				break;
			}
			case R.id.fragment_imageconvert_sdat2img_bt_select_new_dat:{
				FileChooseDialog dialog = new FileChooseDialog(ctx);
					dialog.choose("system.new.dat", new FileChooseDialog.Callback(){

							@Override
							public void onSelected(File file)
							{
								// TODO: Implement this method
								etNewDat.setText(file.getAbsolutePath());
							}
						});
				break;
			}
			case R.id.fragment_imageconvert_sdat2img_bt_select_transfer:{
					FileChooseDialog dialog = new FileChooseDialog(ctx);
					dialog.choose("system.transfer.list", new FileChooseDialog.Callback(){

							@Override
							public void onSelected(File file)
							{
								// TODO: Implement this method
								etTransfer.setText(file.getAbsolutePath());
							}
						});
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
			Looper.prepare();
			convertHandler.sendEmptyMessage(2);
			NativeUtils.sdat2img(etTransfer.getText().toString(), etNewDat.getText().toString(), new File(UnpackbootimgActivity.getOutPath(), etOut.getText().toString() + ".img").getAbsolutePath());
			convertHandler.sendEmptyMessage(0);
		}

	}
	
}

