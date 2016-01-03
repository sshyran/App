package crixec.app.imagefactory.activity;
import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ExceptionHandler;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.utils.FileUtils;
import android.widget.ScrollView;
import android.support.v7.widget.AppCompatTextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener
{
	private AppCompatButton btJoinUs;
	private AppCompatButton btShowSource;
	private AppCompatButton btShowChangeLog;
	private AppCompatButton btShowHelp;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(getResources().getStringArray(R.array.drawer_item_name)[2]);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_about);
		btJoinUs = (AppCompatButton) findViewById(R.id.activity_about_bt_join_us);
		btShowSource = (AppCompatButton) findViewById(R.id.activity_about_bt_show_source);
		btShowChangeLog = (AppCompatButton) findViewById(R.id.activity_about_bt_show_change_log);
		btShowHelp = (AppCompatButton) findViewById(R.id.activity_about_bt_show_help);
		btJoinUs.setOnClickListener(this);
		btShowSource.setOnClickListener(this);
		btShowChangeLog.setOnClickListener(this);
		btShowHelp.setOnClickListener(this);;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		if(item.getItemId() == android.R.id.home){
			setTitle(R.string.app_name);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.activity_about_bt_join_us:{
				joinQQGroup();
				break;
			}
			case R.id.activity_about_bt_show_change_log:{
				String log = "";
				File file = new File(ImageFactory.getStorageDirectory(), "changelog.txt");
					try
					{
						FileUtils.writeFile(getAssets().open("changelog.txt"), file);
						log += FileUtils.readFile(file.getAbsolutePath());
					}
					catch (Exception e)
					{
						ExceptionHandler.handle(e);
					}
					Dialog.build(AboutActivity.this)
					.setTitle("ChangeLog")
					.setMessage(log)
					.setPositiveButton(android.R.string.ok, null)
					.show();
					break;
			}
			case R.id.activity_about_bt_show_source:{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/Crixec/ImageFactory"));
				startActivity(intent);
				break;
			}
			case R.id.activity_about_bt_show_help:{
				Dialog.build(this)
				.setTitle("Help")
				.setMessage(R.string.help_content)
				.setPositiveButton(android.R.string.ok, null)
				.show();
				break;
			}
		}
	}

	/****************
	 *
	 * 发起添加群流程。群号：镜像工厂官方群(456820338) 的 key 为： HWLvhYMCBSd8wVILr2slBFMRQ2usK6o9
	 * 调用 joinQQGroup(HWLvhYMCBSd8wVILr2slBFMRQ2usK6o9) 即可发起手Q客户端申请加群 镜像工厂官方群(456820338)
	 *
	 * @param key 由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup() {
		String key = "HWLvhYMCBSd8wVILr2slBFMRQ2usK6o9";
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}
	
}
