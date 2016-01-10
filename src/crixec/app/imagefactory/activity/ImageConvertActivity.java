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
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Adapter;
import crixec.app.imagefactory.fragment.Simg2imgFragment;
import crixec.app.imagefactory.fragment.Sdat2imgFragment;

public class ImageConvertActivity extends AppCompatActivity{
	
	private AppCompatSpinner spinner;
	private Simg2imgFragment mSimg2img;
	private Sdat2imgFragment mSdat2img;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[5]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_imageconvert);
		mSimg2img = new Simg2imgFragment();
		mSdat2img = new Sdat2imgFragment();
		spinner = (AppCompatSpinner) findViewById(R.id.activity_imageconvert_sp_type);
		spinner.setAdapter(getAdapter());
		getFragmentManager().beginTransaction().add(R.id.activity_imageconvert_framelayout, mSdat2img).hide(mSdat2img).add(R.id.activity_imageconvert_framelayout, mSimg2img).commit();
		spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					if(p3 == 0){
						getFragmentManager().beginTransaction().hide(mSdat2img).show(mSimg2img).commit();
					}else{
						getFragmentManager().beginTransaction().hide(mSimg2img).show(mSdat2img).commit();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1)
				{
					// TODO: Implement this method
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
		return new ArrayAdapter(ImageConvertActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.imageconvert_item_name));
	}
}

