package crixec.app.imagefactory.activity;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.FileComparator;
import crixec.app.imagefactory.utils.XmlDataUtils;
import android.support.v7.app.AlertDialog;
import crixec.app.imagefactory.ui.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ChooserActivity extends AppCompatActivity
{
	private ListView mListView;
	private TextView mTextView;
	private File path;
	private File selected;
	private ArrayList<File> list;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTitle(R.string.choose_a_file);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_chooser);
		if (XmlDataUtils.getString("LAST_PATH").equals(""))
		{
			XmlDataUtils.putString("LAST_PATH", "/");
		}
		String p = XmlDataUtils.getString("LAST_PATH");
		if (!new File(p).canRead())
		{
			p = "/";
			XmlDataUtils.putString("LAST_PATH", "/");
		}
		path = new File(p);
		selected = path;
		mListView = (ListView) findViewById(R.id.activity_chooser_LV_filelist);
		mTextView = (TextView) findViewById(R.id.activity_chooser_TV_current_path);
		mTextView.setText(path.getPath());
		mListView.setAdapter(getAdapter(listDirectory(path)));
		mListView.setOnItemClickListener(new ListView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					final File file = list.get(p3);
					if (file.isDirectory() && !file.canRead())
					{
						Toast.makeLongText(getString(R.string.no_permission_access));
						return;
					}
					else if (file.isDirectory())
					{
						if (p3 == 0)
						{
							path = (file.getParentFile() == null ? file : file.getParentFile());
						}
						else
						{
							path = file;
						}
						XmlDataUtils.putString("LAST_PATH", path.getAbsolutePath());
						mListView.setAdapter(getAdapter(listDirectory(path)));
					}
					else if (file.isFile())
					{
						AlertDialog.Builder dialogBuilder = Dialog.build(ChooserActivity.this);
						dialogBuilder.setTitle(getString(R.string.dialog_choose_this_file))
						.setMessage(file.getAbsolutePath())
						.setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
									selected = file;
									Intent intent = new Intent();
									intent.putExtra("PATH", path.getAbsolutePath());
									intent.putExtra("SELECTED", selected.getAbsolutePath());
									setResult(RESULT_OK, intent);
									finish();
								}
							})
						.setNegativeButton(android.R.string.no, null)
						.show();
					}
					mTextView.setText(path.getPath());
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
	
	public SimpleAdapter getAdapter(ArrayList<File> list)
	{
		ArrayList<HashMap<String, ?>> itemList = new ArrayList<HashMap<String, ?>>();
		for (int i = 0; i < list.size();i++)
		{
			File file = list.get(i);
			int image = R.drawable.ic_file;
			if (file.isDirectory())
			{
				image = R.drawable.ic_directory;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 0)
			{
				map.put("NAME", getString(R.string.parent_path));
				map.put("IMAGE", R.drawable.ic_last_directory);
			}
			else
			{
				map.put("NAME", file.getName());
				map.put("IMAGE", image);
			}
			itemList.add(map);
		}
		return new SimpleAdapter(getApplicationContext(), itemList, R.layout.activity_chooser_item, new String[]{"IMAGE", "NAME"}, new int[]{R.id.activity_chooser_item_IV_image, R.id.activity_chooser_item_TV_name});
	}
	public ArrayList<File> listDirectory(File dir)
	{
		list = new ArrayList<File>();
		File[] childFiles = dir.listFiles();
		for (int i = 0; i < childFiles.length; i++)
		{
			list.add(childFiles[i]);
		}
		Collections.sort(list, new FileComparator());
		list.add(0, dir);
		return list;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if (mTextView.getText().toString().equals("/"))
			{
				setResult(RESULT_CANCELED);
			}
			else{
				path = path.getParentFile();
				mListView.setAdapter(getAdapter(listDirectory(path)));
				mTextView.setText(path.getPath());
				return false;
			}
		} 
		return super.onKeyDown(keyCode, event);
	}

}
