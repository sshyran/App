package crixec.app.imagefactory.ui;
import android.support.v7.app.AppCompatDialog;
import android.content.Context;
import crixec.app.imagefactory.R;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import crixec.app.imagefactory.utils.XmlDataUtils;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.Collections;
import crixec.app.imagefactory.utils.FileComparator;
import android.widget.AdapterView;
import android.view.View;
import crixec.app.imagefactory.core.ExceptionHandler;

public class FileChooseDialog extends AppCompatDialog{
	private Context mContext;
	
	private ListView mListView;
	private TextView mTextView;
	private File path;
	private File selected;
	private ArrayList<File> list;
	private Callback mCallback;
	private boolean finished = false;
	
	
	public FileChooseDialog(Context context){
		super(context);
		mContext = context;
		setContentView(R.layout.dialog_chooser);
	}
	public void choose(String title, Callback callback){
		this.mCallback = callback;
		setTitle(title);
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
						AlertDialog.Builder dialogBuilder = Dialog.build(mContext);
						dialogBuilder.setTitle(getString(R.string.dialog_choose_this_file))
							.setMessage(file.getAbsolutePath())
							.setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener(){

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
									// TODO: Implement this method
									mCallback.onSelected(file);
									dismiss();
								}
							})
							.setNegativeButton(android.R.string.no, null)
							.show();
					}
					mTextView.setText(path.getPath());
				}
			});
			show();
	}

	public SimpleAdapter getAdapter(ArrayList<File> list)
	{
		ArrayList<HashMap<String, ?>> itemList = new ArrayList<HashMap<String, ?>>();
		for (int i = 0; i < list.size();i++)
		{
			File file = list.get(i);
			int image = R.mipmap.ic_file;
			if (file.isDirectory())
			{
				image = R.mipmap.ic_directory;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (i == 0)
			{
				map.put("NAME", getString(R.string.parent_path));
				map.put("IMAGE", R.mipmap.ic_directory);
			}
			else
			{
				map.put("NAME", file.getName());
				map.put("IMAGE", image);
			}
			itemList.add(map);
		}
		return new SimpleAdapter(mContext, itemList, R.layout.dialog_chooser_item, new String[]{"IMAGE", "NAME"}, new int[]{R.id.activity_chooser_item_IV_image, R.id.activity_chooser_item_TV_name});
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
	
	private CharSequence getString(int id)
	{
		// TODO: Implement this method
		return mContext.getString(id);
	}
	public static interface Callback{
		void onSelected(File file);
	}
}
