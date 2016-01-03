package crixec.app.imagefactory.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.utils.RebootUtils;

public class RebootActivity extends AppCompatActivity
{
	private AppCompatSpinner spinner;
	private AppCompatButton btReboot;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.function_item_name)[6]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_reboot);
		spinner = (AppCompatSpinner) findViewById(R.id.activity_reboot_sp_type);
		spinner.setAdapter(getAdapter());
		btReboot = (AppCompatButton) findViewById(R.id.activity_reboot_bt_do_reboot);
		btReboot.setOnClickListener(new AppCompatButton.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					switch(spinner.getSelectedItemPosition()){
						case 0:{
							RebootUtils.recovery();
							break;
						}
						case 1:{
							RebootUtils.reboot();
							break;
						}
						case 2:{
							RebootUtils.bootloader();
							break;
						}
						case 3:{
							RebootUtils.softReboot();
							break;
						}
					}
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
		return new ArrayAdapter(RebootActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.reboot_item_name));
	}
}

