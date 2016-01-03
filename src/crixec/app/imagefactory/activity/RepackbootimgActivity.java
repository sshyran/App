package crixec.app.imagefactory.activity;
import java.io.File;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.DeviceUtils;
import crixec.app.imagefactory.utils.NativeUtils;

public class RepackbootimgActivity extends AppCompatActivity
{
	private AppCompatSpinner spinner;
	private AppCompatEditText outfile;
	private AppCompatButton btRepack;
	public static final String OUTPUT_PREFIX = "Kernel_Repack_";
	
	
	@SuppressLint("HandlerLeak")
	private Handler repackHandler = new Handler(){

		private ProgressDialog dialog;

		@Override
		public void handleMessage(Message msg)
		{
			// TODO: Implement this method
			super.handleMessage(msg);
			if (msg.what == 2)
			{
				dialog = new ProgressDialog(RepackbootimgActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setCancelable(false);
				dialog.setMessage(getString(R.string.repacking));
				dialog.show();
			}
			else
			{
				dialog.dismiss();
				File out = new File(UnpackbootimgActivity.getOutPath(), outfile.getText().toString().trim());
				if (msg.what == 0)
				{
					Dialog.build(RepackbootimgActivity.this)
						.setTitle(R.string.repack_success)
						.setMessage(getString(R.string.repack_kernel_to) + out.getAbsolutePath() + ".img")
						.setPositiveButton(android.R.string.ok, null)
						.show();
				}
				else
				{
					Toast.makeLongText(getString(R.string.repack_failure));
				}
				outfile.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
			}
		}

	};

	private String[] items;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[1]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_repackbootimg);
		spinner = (AppCompatSpinner) findViewById(R.id.activity_repackbootimg_sp_type);
		spinner.setAdapter(getAdapter());
		outfile = (AppCompatEditText) findViewById(R.id.activity_repackbootimg_et_outputfile);
		outfile.setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime());
		btRepack = (AppCompatButton) findViewById(R.id.activity_repackbootimg_bt_do_repack);
		btRepack.setOnClickListener(new AppCompatButton.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					DoRepack repack = new DoRepack();
					repack.start();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayAdapter getAdapter(){
		File outPath = UnpackbootimgActivity.getOutPath();
		ArrayList<String> list = new ArrayList<String>();
		int i = 0;
		for(String file : outPath.list()){
			if(new File(outPath.getAbsolutePath(), file).isDirectory()){
				list.add(file);
			}
		}
		items = new String[list.size()];
		for(String file : list){
			items[i] = file;
			i++;
		}
		return new ArrayAdapter(RepackbootimgActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
	}
	public class DoRepack extends Thread
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
			super.run();
			repackHandler.sendEmptyMessage(2);
			File out = new File(UnpackbootimgActivity.getOutPath(), outfile.getText().toString());
			boolean b = NativeUtils.repackbootimg(UnpackbootimgActivity.getOutPath().getAbsolutePath() + "/" + items[spinner.getSelectedItemPosition()], out.getAbsolutePath() + ".img");
			if (b)
			{
				repackHandler.sendEmptyMessage(0);
			}
			else
			{
				repackHandler.sendEmptyMessage(1);
			}
		}

	}
}

